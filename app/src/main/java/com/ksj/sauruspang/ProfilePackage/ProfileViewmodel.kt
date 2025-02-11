package com.ksj.sauruspang.ProfilePackage

import com.ksj.sauruspang.ProfilePackage.Room.AppDatabase
import com.ksj.sauruspang.ProfilePackage.Room.User
import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Profile(
    var name: String,
    var birth: String,
    var selectedImage: Int,
    var category: String,
    var quizCategory: String,
    var fruitsDayCount: Int = 1,
    var animalsDayCount: Int = 1,
    var colorsDayCount: Int = 1,
    var jobsDayCount: Int = 1
)

class ProfileViewmodel(application: Application) : AndroidViewModel(application) {
    private val _profiles = mutableStateListOf<Profile>()
    val profiles: SnapshotStateList<Profile> get() = _profiles

    private val userDao = AppDatabase.getDatabase(application).userDao()

    init {
        loadProfilesFromDatabase()
    }

    // 프로필 추가 함수
    fun addProfile(
        name: String,
        birth: String,
        selectedImage: Int,
        category: String,
        quizCategory: String,
        fruitsDayCount: Int = 1,
        animalsDayCount: Int = 1,
        colorsDayCount: Int = 1,
        jobsDayCount: Int = 1
    ) {
        val profile = Profile(
            name,
            birth,
            selectedImage,
            category,
            quizCategory,
            fruitsDayCount,
            animalsDayCount,
            colorsDayCount,
            jobsDayCount
        )
        _profiles.add(profile)
        saveProfileToDatabase(profile)
    }

    // 데이터베이스에 프로필 저장 함수
    private fun saveProfileToDatabase(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(
                name = profile.name,
                birth = profile.birth,
                selectedImage = profile.selectedImage,
                category = profile.category,
                quizCategory = profile.quizCategory,
                fruitsDayCount = profile.fruitsDayCount,
                animalsDayCount = profile.animalsDayCount,
                colorsDayCount = profile.colorsDayCount,
                jobsDayCount = profile.jobsDayCount
            )
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
                    user.selectedImage ?: 0,
                    user.category ?: "",
                    user.quizCategory ?: "",
                    user.fruitsDayCount,
                    user.animalsDayCount,
                    user.colorsDayCount,
                    user.jobsDayCount
                )
            }

            withContext(Dispatchers.Main) {
                _profiles.clear()
                _profiles.addAll(updatedProfiles)
            }
        }
    }

    // daycount 를 증가시키는 함수
    fun increaseDayCount(quizCategory: String) {
        val profileIndex = _profiles.indexOfFirst { it.quizCategory == quizCategory }
        if (profileIndex != -1) {
            val currentProfile = _profiles[profileIndex]
            val updatedProfile = when (quizCategory) {
                "과일과 야채" -> currentProfile.copy(fruitsDayCount = currentProfile.fruitsDayCount + 1)
                "동물" -> currentProfile.copy(animalsDayCount = currentProfile.animalsDayCount + 1)
                "색" -> currentProfile.copy(colorsDayCount = currentProfile.colorsDayCount + 1)
                "직업" -> currentProfile.copy(jobsDayCount = currentProfile.jobsDayCount + 1)
                else -> currentProfile
            }

            viewModelScope.launch(Dispatchers.IO) {
                when (quizCategory) {
                    "과일과 야채" -> userDao.updateFruitsDayCount("과일과 야채", updatedProfile.fruitsDayCount)
                    "동물" -> userDao.updateAnimalsDayCount("동물", updatedProfile.animalsDayCount)
                    "색" -> userDao.updateColorsDayCount("색", updatedProfile.colorsDayCount)
                    "직업" -> userDao.updateJobsDayCount("직업", updatedProfile.jobsDayCount)
                }
                withContext(Dispatchers.Main) {
                    _profiles[profileIndex] = updatedProfile
                }
            }
        }
    }
}