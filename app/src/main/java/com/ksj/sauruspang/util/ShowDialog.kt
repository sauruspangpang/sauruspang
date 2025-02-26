package com.ksj.sauruspang.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ksj.sauruspang.R
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
                .fillMaxHeight(0.4f)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = message,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Red, Color.Blue
                                )
                            )
                        )
                    )
                    Text(
                        text = "을(를) 찾고 있어요!", fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DialogCorrect(
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
            color = correctDialogColor,
            modifier = Modifier
                .padding(16.dp)
        ) { MarkCorrect() }
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
                Column {
                    MarkWrong()
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = wrongLetters)
                }
            }
        }
    }
}

@Composable
fun LearnCorrect(
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
            color = correctDialogColor,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
        ) { MarkCorrect() }
    }
}

@Composable
fun LearnRetry(
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(delayTimeMs)
        onDismiss()
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(text = "오답입니다.")
//                Spacer(modifier = Modifier.height(16.dp))
//                // 다이얼로그에서 두 가지 액션이 필요한 경우 두 개의 버튼을 배치할 수 있습니다.
//                Button(onClick = onRetry) {
//                    Text("다시말하기")
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(onClick = onDismiss) {
//                    Text("닫기")
//                }
//            }
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MarkWrong()
            }

        }
    }
}

@Composable
fun CaptureCorrect(
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
            color = correctDialogColor,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
        ) { MarkCorrect() }
    }
}

@Composable
fun CaptureRetry(
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
        ) { MarkWrong() }
    }
}

@Composable
fun MarkCorrect() {
    Image(
        painter = painterResource(id = R.drawable.mark_correct),
        contentDescription = null,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun MarkWrong() {
    Image(
        painter = painterResource(id = R.drawable.mark_wrong),
        contentDescription = null,
        modifier = Modifier.padding(16.dp)
    )
}