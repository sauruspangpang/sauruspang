package com.ksj.sauruspang.Learnpackage.camera

import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel

@Composable
fun GPTRandomPhotoTakerScreen(
    camViewModel: GPTCameraViewModel,
    tts: TextToSpeech?,
    navController: NavController
) {
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    var showResultScreen by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDD4AA))
    ) {
        if (showResultScreen && capturedImage != null) {
            GPTCameraResultScreen(
                capturedImage = capturedImage!!,
                prediction = camViewModel.predictionResult.value,
                onRetake = {
                    showResultScreen = false
                    camViewModel.predictionResult.value = ""
                },
                tts = tts,
                navController = navController

            )
        } else {
            GPTCameraCaptureScreen { bitmap ->
                capturedImage = bitmap
                camViewModel.analyzeImage(bitmap) // 촬영된 이미지 분석 요청
                showResultScreen = true
            }
        }
    }
}
