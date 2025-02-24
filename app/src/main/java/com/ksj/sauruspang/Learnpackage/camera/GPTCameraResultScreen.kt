package com.ksj.sauruspang.Learnpackage.camera

import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.Learnpackage.ScoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GPTCameraResultScreen(
    capturedImage: Bitmap,
    prediction: String,
    onRetake: () -> Unit,
    tts: TextToSpeech?,
    scoreViewModel: ScoreViewModel,
    navController: NavController
) {
    // 간단 예시: Scaffold와 이미지, 텍스트, 버튼을 표시
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("분석 결과", fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFDD4AA))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDD4AA)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                bitmap = capturedImage.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "예측 결과: $prediction", fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetake) {
                Text("다시 촬영")
            }
        }
    }
}
