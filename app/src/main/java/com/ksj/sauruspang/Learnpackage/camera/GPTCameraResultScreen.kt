package com.ksj.sauruspang.Learnpackage.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.ksj.sauruspang.R
import com.ksj.sauruspang.util.LearnCorrect
import com.ksj.sauruspang.util.LearnRetry
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GPTCameraResultScreen(
    capturedImage: Bitmap,
    prediction: String,
    onRetake: () -> Unit,
    tts: TextToSpeech?,
    navController: NavController,
    remainingUsage: Int
) {
    // 예: "사과,Apple" → [ "사과", "Apple" ]
    val words = prediction.split(",").map { it.trim() }
    val koreanWord = words.getOrNull(0) ?: "Unknown"
    val englishWord = words.getOrNull(1) ?: "Unknown"

    // 정답 체크(음성 인식)
    var correctCount by rememberSaveable { mutableIntStateOf(0) }
    // 음성 듣기 함수 (TTS)
    fun listen(text: String, locale: Locale) {
        tts?.language = locale
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    // 음성 인식용 SpeechRecognizer
    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val speechIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }
    }

    // 정답/재시도 다이얼로그
    var showCorrectDialog by remember { mutableStateOf(false) }
    var showRetryDialog by remember { mutableStateOf(false) }

    if (showCorrectDialog) {
        LearnCorrect(
            onDismiss = { showCorrectDialog = false }
        )
    }
    if (showRetryDialog) {
        LearnRetry(
            onDismiss = { showRetryDialog = false },
            onRetry = { showRetryDialog = false }
        )
    }

    // 실제 음성 인식 결과 처리
    var spokenText by remember { mutableStateOf("") }
    val recognitionListener = object : RecognitionListener {
        override fun onResults(results: Bundle?) {
            val detectedMatches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val spoken = detectedMatches?.firstOrNull()?.lowercase(Locale.ROOT) ?: ""
            spokenText = spoken
            // 정답(englishWord)와 일치 시 correctCount++ / 재시도
            if (spoken == englishWord.lowercase(Locale.ROOT)) {
                correctCount++
                showCorrectDialog = true
            } else {
                showRetryDialog = true
            }
        }
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {}
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    }
    speechRecognizer.setRecognitionListener(recognitionListener)

    // 화면 크기
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Scaffold(TopBar 하나만 존재)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // 홈으로 이동 아이콘
                        Image(
                            painter = painterResource(id = R.drawable.image_backhome),
                            contentDescription = "홈으로",
                            modifier = Modifier
                                .size(screenWidth * 0.07f)
                                .clickable {
                                    navController.popBackStack("home", false)
                                }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFDD4AA)
                )
            )
        }
    ) { padding ->
        // 메인 콘텐츠
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFDD4AA))
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.Center)
            ) {
                // 왼쪽(이미지 + 한국어/영어 TTS)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        bitmap = capturedImage.asImageBitmap(),
                        contentDescription = "Captured Image",
                        modifier = Modifier
                            .size(screenWidth * 0.2f)
                            .clickable { listen(englishWord, Locale.US) }
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                    // 한국어
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            koreanWord,
                            modifier = Modifier.clickable { listen(koreanWord, Locale.KOREAN) },
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 50.sp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.listen_btn),
                            contentDescription = "listen button",
                            modifier = Modifier
                                .size(screenWidth * 0.07f)
                                .padding(start = screenWidth * 0.02f)
                                .clickable { listen(koreanWord, Locale.KOREAN) }
                        )
                    }

                    // 영어
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            englishWord,
                            modifier = Modifier.clickable { listen(englishWord, Locale.US) },
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 60.sp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.listen_btn),
                            contentDescription = "listen button",
                            modifier = Modifier
                                .size(screenWidth * 0.07f)
                                .padding(start = screenWidth * 0.02f)
                                .clickable { listen(englishWord, Locale.US) }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(screenWidth * 0.08f))

                // 오른쪽(마이크 버튼 + 정답 체크 아이콘)
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 상단 공백
                    Spacer(modifier = Modifier.height(screenHeight * 0.23f))

                    // 마이크 버튼(음성 인식)
                    Box(
                        modifier = Modifier
                            .size(screenWidth * 0.12f)
                            .shadow(elevation = 10.dp, shape = RoundedCornerShape(16.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF77E4D2),
                                        Color(0xFF4ECDC4)
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                // 마이크 권한 체크
                                val micPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                )
                                if (micPermission != PackageManager.PERMISSION_GRANTED) {
                                    Toast.makeText(context, "마이크 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                                } else {
                                    // 음성 인식 시작
                                    speechRecognizer.startListening(speechIntent)
                                }
                            }
                    ) {
                        // 광택 효과
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.4f),
                                            Color.Transparent
                                        ),
                                        center = Offset(30f, 20f),
                                        radius = 120f
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )
                        Image(
                            painter = painterResource(id = R.drawable.speakbutton),
                            contentDescription = "Speak button",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    // 마이크 아래 약간의 간격
                    Spacer(modifier = Modifier.height(screenHeight * 0.05f))

                    // 정답 체크 아이콘(3개)
                    Row {
                        repeat(3) { index ->
                            Image(
                                painter = painterResource(id = R.drawable.baseline_check_24),
                                contentDescription = "check icon",
                                modifier = Modifier.size(screenWidth * 0.055f),
                                alpha = if (index < correctCount) 1.0f else 0.4f
                            )
                        }
                    }
                }
            }

            // 우측 하단 "다시 찍기 (남은 횟수: ...)" 버튼
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    onClick = onRetake,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("다시 찍기 (남은 횟수: $remainingUsage)")
                }
            }
        }
    }
}
