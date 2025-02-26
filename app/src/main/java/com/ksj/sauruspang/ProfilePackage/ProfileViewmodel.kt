package com.ksj.sauruspang.ProfilePackage

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ksj.sauruspang.ProfilePackage.Room.AppDatabase
import com.ksj.sauruspang.ProfilePackage.Room.CatalogEntry
import com.ksj.sauruspang.ProfilePackage.Room.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

data class Profile(
    var name: String,
    var birth: String,
    var userprofile: Int,
    var selectedImage: Int,
    var score: Int = 0,
    var categoryDayStatus: MutableMap<String, Int> = mutableMapOf()
)

class ProfileViewmodel(application: Application) : AndroidViewModel(application) {
    var profiles = mutableStateListOf<Profile>()
    var selectedProfileIndex = mutableIntStateOf(-1)
    private val userDao = AppDatabase.getDatabase(application).userDao()

    // 퀴즈 관련 기능
    private val _correctCounts = mutableStateMapOf<String, Int>()
    private val _quizSolvedMap = mutableStateMapOf<String, Boolean>()

    init {
        loadProfilesFromDatabase()
    }

    fun selectProfile(index: Int) {
        selectedProfileIndex.value = index
    }

    private fun defaultCategoryStatus(): MutableMap<String, Int> {
        val status = mutableMapOf<String, Int>()
        return status
    }

    fun addProfile(name: String, birth: String, userprofile: Int, selectedImage: Int) {
        val newProfile = Profile(
            name = name,
            birth = birth,
            userprofile = userprofile,
            selectedImage = selectedImage,
            score = 0,
            categoryDayStatus = defaultCategoryStatus()
        )
        profiles.add(newProfile)
        saveProfileToDatabase(newProfile)
    }

    private fun saveProfileToDatabase(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(
                name = profile.name,
                birth = profile.birth,
                selectedImage = profile.selectedImage,
                score = profile.score,
                categoryDayStatus = serializeCategoryDayStatus(profile.categoryDayStatus)
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
                    name = user.name ?: "",
                    birth = user.birth ?: "",
                    userprofile = user.uid,
                    selectedImage = user.selectedImage ?: 0,
                    score = user.score,
                    categoryDayStatus = parseCategoryDayStatus(user.categoryDayStatus)
                )
            })
        }
    }

    private fun parseCategoryDayStatus(status: String?): MutableMap<String, Int> {
        val map = mutableMapOf<String, Int>()
        if (!status.isNullOrEmpty()) {
            status.split(",").forEach { pair ->
                val keyValue = pair.split(":")
                if (keyValue.size == 2) {
                    val key = keyValue[0]
                    val value = keyValue[1].toIntOrNull() ?: 0
                    map[key] = value
                }
            }
        }
        return map
    }

    private fun serializeCategoryDayStatus(map: Map<String, Int>): String {
        return map.entries.joinToString(",") { "${it.key}:${it.value}" }
    }

    fun getActiveDay(categoryName: String): Int {
        val profile = profiles.getOrNull(selectedProfileIndex.value)
        return profile?.categoryDayStatus?.get(categoryName) ?: 1
    }

    fun updateScore(newScore: Int) {
        val index = selectedProfileIndex.value
        if (index in profiles.indices) {
            profiles[index].score = newScore
            updateUserInDatabase(profiles[index])
        }
    }

    fun updateCategoryDayStatus(category: String, newDay: Int) {
        val index = selectedProfileIndex.value
        if (index in profiles.indices) {
            profiles[index].categoryDayStatus[category] = newDay
            updateUserInDatabase(profiles[index])
        }
    }

    private fun updateUserInDatabase(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(
                uid = profile.userprofile,
                name = profile.name,
                birth = profile.birth,
                selectedImage = profile.selectedImage,
                score = profile.score,
                categoryDayStatus = serializeCategoryDayStatus(profile.categoryDayStatus)
            )
            userDao.update(user)
        }
    }

    // 도감 업데이트 – 정답과 촬영된 Bitmap 저장 (동일 answer이면 덮어씌움)
    fun updateCatalogEntry(answer: String, bitmap: Bitmap) {
        val profile = profiles.getOrNull(selectedProfileIndex.value) ?: return
        val profileId = profile.userprofile
        val imageBytes = bitmapToByteArray(bitmap)
        viewModelScope.launch(Dispatchers.IO) {
            val entry = CatalogEntry(profileId = profileId, answer = answer, image = imageBytes)
            AppDatabase.getDatabase(getApplication()).catalogEntryDao().insertCatalogEntry(entry)
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val resizedBitmap = resizeBitmap(bitmap, 500, 500)
        val stream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }

    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= maxWidth && height <= maxHeight) return bitmap
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
        val finalWidth: Int
        val finalHeight: Int
        if (ratioBitmap > ratioMax) {
            finalWidth = maxWidth
            finalHeight = (maxWidth / ratioBitmap).toInt()
        } else {
            finalHeight = maxHeight
            finalWidth = (maxHeight * ratioBitmap).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
    }

    fun getCatalogEntries() = AppDatabase.getDatabase(getApplication())
        .catalogEntryDao()
        .getCatalogEntriesForProfile(profiles.getOrNull(selectedProfileIndex.value)?.userprofile ?: -1)

    fun deleteProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(
                uid = profile.userprofile,
                name = profile.name,
                birth = profile.birth,
                selectedImage = profile.selectedImage,
                score = profile.score,
                categoryDayStatus = serializeCategoryDayStatus(profile.categoryDayStatus)
            )
            userDao.deleteById(user.uid)
        }
        profiles.remove(profile)
    }

    // --- 퀴즈 관련 기능 ---
    fun increaseCorrectCount(questionId: String) {
        _correctCounts[questionId] = (_correctCounts[questionId] ?: 0) + 1
    }

    fun getCorrectCount(questionId: String): Int = _correctCounts[questionId] ?: 0

    fun isQuizSolved(questionId: String): Boolean = _quizSolvedMap[questionId] ?: false

    fun markQuizAsSolved(questionId: String) {
        _quizSolvedMap[questionId] = true
    }
}
