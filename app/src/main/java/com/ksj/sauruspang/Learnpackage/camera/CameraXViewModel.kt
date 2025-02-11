package com.ksj.sauruspang.Learnpackage.camera

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.Learnpackage.QuizQuestion

// 더미 비트맵 생성 함수 (필요시 다른 곳에서도 재사용 가능)
fun createDummyBitmap(width: Int = 100, height: Int = 100): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // 배경 색상 설정
    canvas.drawColor(Color.BLACK)

    // 간단한 텍스트 추가 (선택 사항)
    val paint = Paint().apply {
        color = Color.RED
        textSize = 20f
        isAntiAlias = true
    }
    canvas.drawText("Dummy", 10f, height / 2f, paint)

    return bitmap
}

class CameraViewModel : ViewModel() {
    // 고정된 더미 비트맵 인스턴스를 한 번 생성
    val dummyBitmpa: Bitmap = createDummyBitmap()

    var capturedImage: MutableState<Bitmap?> = mutableStateOf(dummyBitmpa)
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
        capturedImage.value = dummyBitmpa
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
