package com.ksj.sauruspang.ProfilePackage

import com.ksj.sauruspang.ProfilePackage.Room.AppDatabase
import com.ksj.sauruspang.ProfilePackage.Room.User
import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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
    var hitNumber: Int = 0
)



class ProfileViewmodel(application: Application) : AndroidViewModel(application) {
    var profiles = mutableStateListOf<Profile>()
    private val userDao = AppDatabase.getDatabase(application).userDao()

    init {
        loadProfilesFromDatabase()
    }

    fun addProfile(name: String, birth: String, userprofile: Int, selectedImage: Int) {
        profiles.add(Profile(name, birth, userprofile, selectedImage))
        saveProfileToDatabase(name, birth, selectedImage)
    }

    private fun saveProfileToDatabase(name: String, birth: String, selectedImage: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(name = name, birth = birth, selectedImage = selectedImage)
            userDao.insert(user)
        }
    }

    private fun loadProfilesFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = userDao.getAll().first()
            profiles.clear()
            profiles.addAll(users.map { user ->
                Profile(user.name ?: "", user.birth ?: "", user.uid, user.selectedImage ?: 0)
            })
        }
    }
    private val _correctCounts = mutableStateMapOf<String, Int>() // Map to store counts per question
    private val _quizSolvedMap = mutableStateMapOf<String, Boolean>()

    fun increaseCorrectCount(questionId: String) {
        _correctCounts[questionId] = (_correctCounts[questionId] ?: 0) + 1
    }

    fun getCorrectCount(questionId: String): Int {
        return _correctCounts[questionId] ?: 0
    }

    // Function to get whether the quiz question is solved
    fun isQuizSolved(questionId: String): Boolean {
        return _quizSolvedMap[questionId] ?: false
    }

    // Function to set the quiz question as solved
    fun markQuizAsSolved(questionId: String) {
        _quizSolvedMap[questionId] = true
    }



}