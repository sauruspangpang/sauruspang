package com.ksj.sauruspang.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

const val delayTimeMs = 2000L

@Composable
fun DialogCorrect(
    message: String = "정답입니다!",
    onDismiss: () -> Unit
) {
    // 다이얼로그가 열리면 2초 후 자동으로 onDismiss() 호출
    LaunchedEffect(Unit) {
        delay(delayTimeMs)
        onDismiss()
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFDBE5FF),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO 오답 이미지 또는 아이콘 변경 필요
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = message)
            }
        }
    }
}

@Composable
fun DialogRetry(
    wrongLetters: String = "틀린 글자 정보",
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    // 다이얼로그가 열리면 2초 후 자동으로 onDismiss() 호출
    LaunchedEffect(Unit) {
        delay(delayTimeMs)
        onDismiss()
        onRetry()
    }
    Dialog(onDismissRequest = {
        onDismiss()
        onRetry()
    }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFFDBDB),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO 오답 이미지 또는 아이콘 변경 필요
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "다시 한 번 풀어보세요.")
                    // TODO 틀린 단어 출력 할 지 결정 필요
                    Text(text = wrongLetters)
                }
            }
        }
    }
}
