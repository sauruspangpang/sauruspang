package com.ksj.sauruspang.Learnpackage.camera

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.Learnpackage.QuizQuestion

class CameraViewModel : ViewModel() {
    var capturedImage: MutableState<Bitmap?> = mutableStateOf(null)
        private set
    var answerWord: String = ""
    var isCorrect: Boolean = false
    var correctFruitImageList: MutableList<Bitmap> = mutableStateListOf()
        private set
    var correctFruitWordList: MutableList<String> = mutableListOf()
    var correctAnimalImageList: MutableList<Bitmap> = mutableStateListOf()
        private set
    var correctAnimalWordList: MutableList<String> = mutableListOf()
    var correctColorImageList: MutableList<Bitmap> = mutableStateListOf()
        private set
    var correctColorWordList: MutableList<String> = mutableListOf()
    var correctJobImageList: MutableList<Bitmap> = mutableStateListOf()
        private set
    var correctJobWordList: MutableList<String> = mutableListOf()

    fun setCapturedImage(bitmap: Bitmap) {
        capturedImage.value = bitmap
    }

    fun clearImage() {
        capturedImage.value = null
    }
}

class SharedRouteViewModel : ViewModel() {
    var sharedValue: String? = null // 저장할 값
    var sharedCategory: QuizCategory? = null
    var sharedClickCount: Int = 0
    var sharedFront: String = ""
    var sharedPopUp: String = ""
    var sharedQuestionIndex: Int = 0
    var sharedQuestions: List<QuizQuestion> = emptyList()
    var sharedQuestion: QuizQuestion = QuizQuestion(1, "", "")
    var sharedBack: String = ""
    var sharedCategoryName: String = ""

    fun clearValue() {
        sharedValue = null
    }
}

class DetectedResultListViewModel : ViewModel() {
    var detectedResultList: List<String>? = null

    fun clearValue() {
        detectedResultList = null
    }
}
