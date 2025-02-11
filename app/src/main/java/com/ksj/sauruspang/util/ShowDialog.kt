package com.ksj.sauruspang.util

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.ksj.sauruspang.Learnpackage.ScoreViewModel
import kotlinx.coroutines.delay

const val delayTimeMs = 2000L
val correctDialogColor = Color(0xFFDBE5FF)
val retryDialogColor = Color(0xFFFFDBDB)

@Composable
fun LoadingDialog(message: String) {
    // Dialog를 사용하여 팝업 형태로 표시합니다.
    Dialog(onDismissRequest = { /* 팝업은 자동으로 닫히므로 외부에서 dismiss 처리합니다. */ }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
        ) {
            // 간단한 로딩 인디케이터와 메시지 표시
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = message)
                }
            }
        }
    }
}

@Composable
fun DialogCorrect(
    message: String = "정답입니다!",
    scoreViewModel: ScoreViewModel,
    onDismiss: () -> Unit
) {
    // 다이얼로그가 열리면 2초 후 자동으로 onDismiss() 호출
    LaunchedEffect(Unit) {
        scoreViewModel.incrementScore()
        delay(delayTimeMs)
        onDismiss()
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = correctDialogColor,
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
            color = retryDialogColor,
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

@Composable
fun LearnCorrect(
    message: String = "정답입니다!",
    scoreViewModel: ScoreViewModel,
    onDismiss: () -> Unit
) {
    // 다이얼로그가 열리면 2초 후 자동으로 onDismiss() 호출
    LaunchedEffect(Unit) {
        scoreViewModel.incrementScore()
        delay(delayTimeMs)
        onDismiss()
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = correctDialogColor,
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
fun LearnRetry(
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "오답입니다.")
                Spacer(modifier = Modifier.height(16.dp))
                // 다이얼로그에서 두 가지 액션이 필요한 경우 두 개의 버튼을 배치할 수 있습니다.
                Button(onClick = onRetry) {
                    Text("다시말하기")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onDismiss) {
                    Text("닫기")
                }
            }
        }
    }
}

@Composable
fun CaptureCorrect(
    message: String = "정답입니다!",
    scoreViewModel: ScoreViewModel,
    onDismiss: () -> Unit
) {
    // 다이얼로그가 열리면 2초 후 자동으로 onDismiss() 호출
    LaunchedEffect(Unit) {
        scoreViewModel.incrementScore()
        delay(delayTimeMs)
        onDismiss()
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        // 다이얼로그 UI 스타일을 적용하기 위해 Surface를 사용합니다.
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = correctDialogColor,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = message)
                // 버튼으로 팝업 닫기 할 경우
//                Spacer(modifier = Modifier.height(16.dp))
//                Button(onClick = onDismiss) {
//                    Text("확인")
//                }
            }
        }
    }
}

@Composable
fun CaptureRetry(
    message: String = "다시 한 번 찍어보세요.",
    onDismiss: () -> Unit
) {
    // 다이얼로그가 열리면 2초 후 자동으로 onDismiss() 호출
    LaunchedEffect(Unit) {
        delay(delayTimeMs)
        onDismiss()
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        // 다이얼로그 UI 스타일을 적용하기 위해 Surface를 사용합니다.
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = retryDialogColor,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = message)
                // 버튼으로 팝업 닫기 할 경우
//                Spacer(modifier = Modifier.height(16.dp))
//                Button(onClick = onDismiss) {
//                    Text("확인")
//                }
            }
        }
    }
}