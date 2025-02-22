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

// Profile 데이터 클래스에 score와 categoryDayStatus를 추가
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

    // 퀴즈 관련 기능: 문제별 정답 횟수와 해결 여부 관리
    private val _correctCounts = mutableStateMapOf<String, Int>()
    private val _quizSolvedMap = mutableStateMapOf<String, Boolean>()

    init {
        loadProfilesFromDatabase()
    }

    fun selectProfile(index: Int) {
        selectedProfileIndex.value = index
    }

    // 기본 카테고리 상태: 예시로 "Math", "Science", "History", "Language"를 1로 설정
    private fun defaultCategoryStatus(): MutableMap<String, Int> {
        val status = mutableMapOf<String, Int>()
        listOf("Math", "Science", "History", "Language").forEach { category ->
            status[category] = 1
        }
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

    // 도감(사진첩) 업데이트 함수 – 정답(answer)와 촬영된 Bitmap을 저장 (동일 answer면 덮어씌움)
    fun updateCatalogEntry(answer: String, bitmap: Bitmap) {
        val profile = profiles.getOrNull(selectedProfileIndex.value) ?: return
        val profileId = profile.userprofile
        val imageBytes = bitmapToByteArray(bitmap)
        viewModelScope.launch(Dispatchers.IO) {
            val entry = CatalogEntry(profileId = profileId, answer = answer, image = imageBytes)
            AppDatabase.getDatabase(getApplication()).catalogEntryDao().insertCatalogEntry(entry)
        }
    }

    // Updated bitmapToByteArray: resize the bitmap before compressing.
    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        // Resize the bitmap to a maximum of 500x500 (adjust as needed)
        val resizedBitmap = resizeBitmap(bitmap, 500, 500)
        val stream = ByteArrayOutputStream()
        // Use JPEG compression with quality 80 (you can adjust quality to reduce size further)
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        return stream.toByteArray()
    }

    // Helper function to resize a Bitmap maintaining aspect ratio.
    private fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }
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

    // 현재 프로필의 도감 항목을 Flow로 반환
    fun getCatalogEntries(): kotlinx.coroutines.flow.Flow<List<CatalogEntry>> {
        val profile = profiles.getOrNull(selectedProfileIndex.value)
        return if (profile != null) {
            AppDatabase.getDatabase(getApplication()).catalogEntryDao().getCatalogEntriesForProfile(profile.userprofile)
        } else {
            kotlinx.coroutines.flow.emptyFlow()
        }
    }

    // --- 퀴즈 관련 기능 ---

    fun increaseCorrectCount(questionId: String) {
        _correctCounts[questionId] = (_correctCounts[questionId] ?: 0) + 1
    }

    fun getCorrectCount(questionId: String): Int {
        return _correctCounts[questionId] ?: 0
    }

    fun isQuizSolved(questionId: String): Boolean {
        return _quizSolvedMap[questionId] ?: false
    }

    fun markQuizAsSolved(questionId: String) {
        _quizSolvedMap[questionId] = true
    }
}
