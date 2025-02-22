package com.ksj.sauruspang.ProfilePackage

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ksj.sauruspang.ProfilePackage.Room.AppDatabase
import com.ksj.sauruspang.ProfilePackage.Room.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Profile 데이터 클래스에 score와 categoryDayStatus(각 카테고리별 활성화 day)를 추가
data class Profile(
    var name: String,
    var birth: String,
    var userprofile: Int,
    var selectedImage: Int,
    var score: Int = 0,
    // 예시로 "Math", "Science", "History", "Language" 카테고리의 활성화된 day를 저장
    var categoryDayStatus: MutableMap<String, Int> = mutableMapOf()
)

class ProfileViewmodel(application: Application) : AndroidViewModel(application) {
    var profiles = mutableStateListOf<Profile>()
    var selectedProfileIndex = mutableIntStateOf(-1)
    private val userDao = AppDatabase.getDatabase(application).userDao()

    // 퀴즈 관련 기능: 문제별 정답 횟수와 해결 여부를 관리하는 Map
    private val _correctCounts = mutableStateMapOf<String, Int>()
    private val _quizSolvedMap = mutableStateMapOf<String, Boolean>()

    init {
        loadProfilesFromDatabase()
    }

    fun selectProfile(index: Int) {
        selectedProfileIndex.value = index
    }

    // 프로필 생성 시 각 카테고리의 기본 활성화 day를 1로 설정 (예시)
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

    // 문자열(예:"Math:1,Science:2")을 파싱하여 Map으로 변환
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

    // Map을 문자열로 직렬화 (예:"Math:1,Science:2")
    private fun serializeCategoryDayStatus(map: Map<String, Int>): String {
        return map.entries.joinToString(",") { "${it.key}:${it.value}" }
    }

    // 선택된 프로필의 특정 카테고리 활성화 day를 반환 (기본값 1)
    fun getActiveDay(categoryName: String): Int {
        val profile = profiles.getOrNull(selectedProfileIndex.value)
        return profile?.categoryDayStatus?.get(categoryName) ?: 1
    }

    // 프로필의 score를 업데이트한 후 데이터베이스에 반영
    fun updateScore(newScore: Int) {
        val index = selectedProfileIndex.value
        if (index in profiles.indices) {
            profiles[index].score = newScore
            updateUserInDatabase(profiles[index])
        }
    }

    // 특정 카테고리의 활성화 day를 업데이트한 후 데이터베이스에 반영
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

    // --- 퀴즈 관련 기능 ---

    // 해당 questionId의 정답 횟수를 증가시킵니다.
    fun increaseCorrectCount(questionId: String) {
        _correctCounts[questionId] = (_correctCounts[questionId] ?: 0) + 1
    }

    // 해당 questionId의 정답 횟수를 반환합니다.
    fun getCorrectCount(questionId: String): Int {
        return _correctCounts[questionId] ?: 0
    }

    // 해당 questionId의 문제 해결 여부를 반환합니다.
    fun isQuizSolved(questionId: String): Boolean {
        return _quizSolvedMap[questionId] ?: false
    }

    // 해당 questionId의 문제를 해결된 상태로 표시합니다.
    fun markQuizAsSolved(questionId: String) {
        _quizSolvedMap[questionId] = true
    }
}
