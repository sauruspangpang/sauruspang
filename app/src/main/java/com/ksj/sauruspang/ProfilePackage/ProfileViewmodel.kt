package com.ksj.sauruspang.ProfilePackage

import com.ksj.sauruspang.ProfilePackage.Room.AppDatabase
import com.ksj.sauruspang.ProfilePackage.Room.User
import android.app.Application
import android.provider.UserDictionary.Words
import android.util.Base64
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksj.sauruspang.ProfilePackage.Room.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.sql.Blob

data class Profile(
    var name: String,
    var birth: String,
    var userprofile: Int,
    var selectedImage: Int,
    var clearedImage: List<String>,
    var clearedWords: List<String>
)

class ProfileViewmodel(application: Application) : AndroidViewModel(application) {
    var profiles = mutableStateListOf<Profile>()
    private val userDao = AppDatabase.getDatabase(application).userDao()

    init {
        loadProfilesFromDatabase()
    }

    fun addProfile(
        name: String,
        birth: String,
        userprofile: Int,
        selectedImage: Int,
        clearedImage: List<String>,
        clearedWords: List<String>
    ) {
        profiles.add(Profile(name, birth, userprofile, selectedImage, clearedImage, clearedWords))
        saveProfileToDatabase(name, birth, selectedImage, clearedImage, clearedWords)
    }

    private fun saveProfileToDatabase(
        name: String,
        birth: String,
        selectedImage: Int,
        clearedImage: List<String>,
        clearedWords: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(
                name = name,
                birth = birth,
                selectedImage = selectedImage,
                clearedImage = clearedImage,
                clearedWords = clearedWords
            )
            userDao.insert(user)
        }
    }

    private fun loadProfilesFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = userDao.getAll().first()
            profiles.clear()
            profiles.addAll(users.map { user ->
                Profile(
                    user.name ?: "",
                    user.birth ?: "",
                    user.uid,
                    user.selectedImage ?: 0,
                    user.clearedImage ?: emptyList(),
                    user.clearedWords ?: emptyList()
                )
            })
        }
    }
}

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    fun saveUser(
        name: String?,
        birth: String?,
        selectedImage: Int?,
        clearedImages: List<ByteArray>?,
        clearedWords: List<String>?
    ) {
        val convertedImages = clearedImages?.map { Base64.encodeToString(it, Base64.DEFAULT) }
        val convertedWords = clearedWords?.joinToString(",")

        val user = User(
            name = name,
            birth = birth,
            selectedImage = selectedImage,
            clearedImage = convertedImages,
            clearedWords = clearedWords
        )

        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }

    fun loadUser(uid: Int, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUser(uid)
            callback(user)
        }
    }
}