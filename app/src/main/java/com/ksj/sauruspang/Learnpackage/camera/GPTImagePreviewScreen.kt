package com.ksj.sauruspang.Learnpackage.camera

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color


@Composable
fun GPTImagePreviewScreen(
    capturedImage: Bitmap,
    prediction: String,
    onRetake: () -> Unit,
    onAnalyze: () -> Unit
) {
    var displayText by remember { mutableStateOf(prediction) }

    // 📌 GPT 응답이 변경될 때 UI 업데이트
    LaunchedEffect(prediction) {
        displayText = prediction
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE0B2))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                bitmap = capturedImage.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp)
            )
            Row {
                Button(onClick = onRetake) {
                    Text("다시 촬영")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onAnalyze) {
                    Text("확인")
                }
            }
            // 📌 GPT 결과 표시 부분
            if (displayText.isNotEmpty()) {
                Text(
                    text = "🔍 분석 결과: $displayText",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

