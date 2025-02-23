package com.ksj.sauruspang.Learnpackage.word

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.Learnpackage.ScoreViewModel
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R
import com.ksj.sauruspang.util.LearnCorrect
import com.ksj.sauruspang.util.LearnRetry
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordQuizScreen(
    navController: NavController,
    categoryName: String,
    dayIndex: Int,
    questionIndex: Int,
    tts: TextToSpeech?,
    viewModel: ProfileViewmodel,
    scoreViewModel: ScoreViewModel,

    ) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]

    var progress by remember { mutableFloatStateOf(0.2f) } // Example progress (50%)

    fun listen(text: String, locale: Locale) {
        tts?.language = locale
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }


    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val speechIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }
    }
    var showCorrectDialog by remember { mutableStateOf(false) }
    var showRetryDialog by remember { mutableStateOf(false) }


    val coroutineScope = rememberCoroutineScope()

    if (showCorrectDialog) {
        LearnCorrect(
            scoreViewModel = scoreViewModel,
            onDismiss = { showCorrectDialog = false }
        )
    }

    if (showRetryDialog) {
        LearnRetry(
            onDismiss = { showRetryDialog = false },
            onRetry = {
                // 다시쓰기 동작 수행 (예: 캔버스 초기화)
                // recognizedText = "Recognition Result: "
                showRetryDialog = false
            }
        )
    }
    var spokenText by remember { mutableStateOf("") }
//    var correctCount by rememberSaveable { mutableIntStateOf(0) }
//    var completedQuestion by rememberSaveable { mutableStateOf(false) }
    val questionId = "$categoryName-$dayIndex-$questionIndex"
    val correctCount by remember { derivedStateOf { viewModel.getCorrectCount(questionId) } }
    val completedQuestion by remember { derivedStateOf { correctCount > 2 } }

    val recognitionListener = object : RecognitionListener {
        override fun onResults(results: Bundle?) {
            val detectedMatches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val spoken = detectedMatches?.firstOrNull()?.lowercase(Locale.ROOT) ?: ""
            spokenText = spoken
            if (spoken == question.english.lowercase(Locale.ROOT)) {
                viewModel.increaseCorrectCount(questionId)
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
//    var hasPermission by remember { mutableStateOf(false) }

    speechRecognizer.setRecognitionListener(recognitionListener)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.question_wallpaper),
            contentDescription = " ",
            contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
            modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
        )

        Image(
            painter = painterResource(id = R.drawable.image_backhome),
            contentDescription = "",
            modifier = Modifier
                .size(screenWidth * 0.07f)
                .clickable {
                    category?.name?.let { categoryName ->
                        navController.popBackStack("stage/$categoryName", false)
                    }
                }
        )


        // 배경이미지 설정

        Image(
            painter = painterResource(id = R.drawable.image_backarrow),
            contentDescription = "previous question",
            alpha = if (questionIndex==0) 0.0f else 1.0f,
            modifier = Modifier
                .size(screenWidth * 0.155f, screenHeight*0.25f)
                .align(Alignment.CenterStart)
                .clickable {
                    if (questionIndex > 0) {
                        navController.navigate("learn/$categoryName/$dayIndex/${questionIndex - 1}") {
                            popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = true }
                        }
                    }
                }
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.1f))
                Image(painter = painterResource(id = question.imageId),
                    contentDescription = "question image",
                    modifier = Modifier
                        .size(screenWidth * 0.2f)
                        .clickable { listen(question.english, Locale.US) }

                )
                Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        question.korean,
                        modifier = Modifier.clickable {
                            listen(
                                question.korean,
                                Locale.KOREAN
                            )
                        },
                        style = TextStyle(
                            fontWeight = FontWeight.Bold, fontSize = 50.sp
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.listen_btn),
                        contentDescription = "listen button",
                        modifier = Modifier
                            .size(screenWidth * 0.07f)
                            .padding(start = screenWidth * 0.02f)
                            .clickable { listen(question.korean, Locale.KOREAN) }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        question.english,
                        modifier = Modifier.clickable { listen(question.english, Locale.US) },
                        style = TextStyle(
                            fontWeight = FontWeight.Bold, fontSize = 60.sp
                        )
                    )
                    Image(painter = painterResource(id = R.drawable.listen_btn),
                        contentDescription = "listen button",
                        modifier = Modifier
                            .size(screenWidth * 0.07f)
                            .padding(start = screenWidth * 0.02f)
                            .clickable { listen(question.english, Locale.US) })

                }
            }
            Spacer(modifier = Modifier.width(screenWidth * 0.08f))
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(screenHeight * 0.23f))
                Box(
                    modifier = Modifier
                        .size(screenWidth * 0.12f)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(16.dp)
                        ) // Increased shadow for more visibility

                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF77E4D2), // Bright turquoise
                                    Color(0xFF4ECDC4)  // Slightly darker shade
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            val micPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            )
                            if (micPermission != PackageManager.PERMISSION_GRANTED) {
                                Toast
                                    .makeText(context, "마이크 권한이 필요합니다.", Toast.LENGTH_SHORT)
                                    .show()
                            } else
                                speechRecognizer.startListening(speechIntent)
                        }
                ) {
                    // Glossy effect overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.4f), // Shiny highlight
                                        Color.Transparent
                                    ),
                                    center = Offset(30f, 20f), // Light source effect
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
                        contentScale = ContentScale.Fit // Ensures it fills the box
                    )
                }
                Spacer(modifier = Modifier.height(screenHeight * 0.05f))
                Row {
                    repeat(3) { index ->
                        Image(
                            painter = painterResource(id = question.imageId),
                            contentDescription = "listen button",
                            modifier = Modifier.size(screenWidth * 0.055f),
                            alpha = if (index < correctCount) 1.0f else 0.4f
                        )
                        //index 0 = image1, index1 = image2, index2 = image3
                    }

                }

            }

        }
        Image(
            painter = painterResource(id = R.drawable.image_frontarrow),
            contentDescription = "",
            modifier = Modifier
                .size(screenWidth * 0.155f)
                .align(Alignment.CenterEnd)
                .offset(x = -(screenWidth * 0.03f))
                .clickable//(enabled = completedQuestion)
                {
                    navController.navigate("WordInput/$categoryName/$dayIndex/${questionIndex}")
                },
            colorFilter = if (completedQuestion) null else ColorFilter.tint(Color.Gray)
        )
    }
}


