package com.ksj.sauruspang.Learnpackage.camera

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    var capturedImage: MutableState<Bitmap?> = mutableStateOf(null)
        private set

    fun setCapturedImage(bitmap: Bitmap) {
        capturedImage.value = bitmap
    }

    fun clearImage() {
        capturedImage.value = null
    }
}

class SharedRouteViewModel : ViewModel() {
    var sharedValue: String? = null // 저장할 값

    fun clearValue(){
        sharedValue = null
    }
}

class DetectedResultListViewModel : ViewModel(){
    var detectedResultList: List<String>? = null
    var answerWord: String? = null

    fun clearValue(){
        detectedResultList = null
    }
}
