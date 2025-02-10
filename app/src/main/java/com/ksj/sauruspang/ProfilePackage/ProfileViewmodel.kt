package com.ksj.sauruspang.ProfilePackage

import com.ksj.sauruspang.ProfilePackage.Room.AppDatabase
import com.ksj.sauruspang.ProfilePackage.Room.User
import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class Profile(
    var name: String,
    var birth: String,
    var userprofile: Int,
    var selectedImage : Int,
    var category : String,
    var quizCategory: String
)

class ProfileViewmodel(application: Application) : AndroidViewModel(application) {
    var profiles = mutableStateListOf<Profile>()
    private val userDao = AppDatabase.getDatabase(application).userDao()

    init {
        loadProfilesFromDatabase()
    }

    fun addProfile(name: String, birth: String, userprofile: Int, selectedImage: Int, category: String, quizCategory: String) {
        profiles.add(Profile(name, birth, userprofile, selectedImage, category, quizCategory))
        saveProfileToDatabase(name, birth, selectedImage, category, quizCategory)
    }

    private fun saveProfileToDatabase(name: String, birth: String, selectedImage: Int, category: String, quizCategory: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(name = name, birth = birth, selectedImage = selectedImage, category = category, quizCategory = quizCategory)
            userDao.insert(user)
        }
    }

    private fun loadProfilesFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = userDao.getAll().first()
            profiles.clear()
            profiles.addAll(users.map { user ->
                Profile(user.name ?: "", user.birth ?: "", user.uid, user.selectedImage ?: 0, user.category ?: "", user.quizCategory ?: "")
            })
        }
    }
}