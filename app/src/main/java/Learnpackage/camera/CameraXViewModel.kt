package Learnpackage.camera

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