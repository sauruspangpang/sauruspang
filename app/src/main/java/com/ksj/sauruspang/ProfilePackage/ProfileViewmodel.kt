package com.ksj.sauruspang.ProfilePackage

import com.ksj.sauruspang.ProfilePackage.Room.AppDatabase
import com.ksj.sauruspang.ProfilePackage.Room.User
import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Profile(
    var name: String,
    var birth: String,
    var userprofile: Int,
    var selectedImage : Int,
    var category : String,
    var quizCategory: String,
    var dayCount: Int
)
class ProfileViewmodel(application: Application) : AndroidViewModel(application) {
    private val _profiles = mutableStateListOf<Profile>() // State로 관리
    val profiles: List<Profile> get() = _profiles

    private val userDao = AppDatabase.getDatabase(application).userDao()

    init {
        loadProfilesFromDatabase()
    }

    // 프로필 추가 함수
    fun addProfile(name: String, birth: String, userprofile: Int, selectedImage: Int, category: String, quizCategory: String, dayCount: Int) {
        val profile = Profile(name, birth, userprofile, selectedImage, category, quizCategory, dayCount)
        _profiles.add(profile)
        saveProfileToDatabase(name, birth,selectedImage, category, quizCategory, dayCount)
    }

    // 데이터베이스에 프로필 저장 함수
    private fun saveProfileToDatabase(name: String, birth: String, selectedImage: Int, category: String, quizCategory: String, dayCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(name = name, birth = birth, selectedImage = selectedImage, category = category, quizCategory = quizCategory, dayCount = dayCount)
            userDao.insert(user)
        }
    }

    // 데이터베이스에서 프로필 불러오기
    private fun loadProfilesFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = userDao.getAll().first()
            val updatedProfiles = users.map { user ->
                Profile(
                    user.name ?: "",
                    user.birth ?: "",
                    user.uid,
                    user.selectedImage ?: 0,
                    user.category ?: "",
                    user.quizCategory ?: "",
                    user.dayCount ?: 1
                )
            }

            withContext(Dispatchers.Main) {
                _profiles.clear()
                _profiles.addAll(updatedProfiles)
            }
        }
    }

    // dayCount를 증가시키는 함수
    fun increaseDayCount(categoryName: String) {
        // categoryName에 해당하는 프로필 찾기
        val profileIndex = _profiles.indexOfFirst { it.category == categoryName }
        if (profileIndex != -1) {
            val currentProfile = _profiles[profileIndex]
            val updatedProfile = currentProfile.copy(dayCount = currentProfile.dayCount + 1)

            // UI 상태 갱신
            _profiles[profileIndex] = updatedProfile

            // 데이터베이스 업데이트
            viewModelScope.launch(Dispatchers.IO) {
                userDao.updateDayCount(categoryName, updatedProfile.dayCount)
            }
        }
    }
}