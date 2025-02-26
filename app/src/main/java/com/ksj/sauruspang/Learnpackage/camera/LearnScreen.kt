package com.ksj.sauruspang.Learnpackage.camera

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.Learnpackage.ScoreViewModel
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R
import com.ksj.sauruspang.util.LearnCorrect
import com.ksj.sauruspang.util.LearnRetry
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LearnScreen(
    navController: NavController,
    categoryName: String,
    dayIndex: Int,
    questionIndex: Int,
    tts: TextToSpeech?,
    viewModel: ProfileViewmodel,
    sharedRouteViewModel: SharedRouteViewModel,
    scoreViewModel: ScoreViewModel
) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    var progress by remember { mutableFloatStateOf(0.2f) }
    var showPopup by remember { mutableStateOf(false) }

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
            onRetry = { showRetryDialog = false }
        )
    }
    var spokenText by remember { mutableStateOf("") }
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
                // 점수 업데이트: 기존 점수에 5점 추가
                val currentScore = viewModel.profiles.getOrNull(viewModel.selectedProfileIndex.value)?.score ?: 0
                viewModel.updateScore(currentScore + 5)
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

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper_learnscreen),
            contentDescription = " ",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .zIndex(-1f)
        )
        Image(painter = painterResource(id = R.drawable.icon_backtochooseda),
            contentDescription = "",
            modifier = Modifier
                .clickable {
                    category?.name?.let { categoryName ->
                        navController.popBackStack("stage/$categoryName", false)
                    }
                }
        )

        Image(painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = "previous question",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(enabled = questionIndex > 0) {
                    if (questionIndex > 0) {
                        navController.popBackStack(
                            "camera/$categoryName/$dayIndex/${questionIndex - 1}",
                            false
                        )
                    } else {
                        navController.popBackStack()
                    }
                })
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
                            listen(question.korean, Locale.KOREAN)
                        },
                        style = TextStyle(
                            fontWeight = FontWeight.Bold, fontSize = 50.sp
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.icon_readword),
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
                    Image(painter = painterResource(id = R.drawable.icon_readword),
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
                    Image(
                        painter = painterResource(id = R.drawable.icon_speakword),
                        contentDescription = "Speak button",
                        modifier = Modifier
                            .padding(8.dp),
                    )
                }
                Row {
                    repeat(3) { index ->
                        Image(
                            painter = painterResource(id = question.imageId),
                            contentDescription = "listen button",
                            modifier = Modifier.size(screenWidth * 0.055f),
                            colorFilter = if (index < correctCount) null else ColorFilter.colorMatrix(
                                ColorMatrix().apply {
                                    setToSaturation(0.3f)
                                }
                            ),
                        )
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = "next question",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(/*enabled = completedQuestion*/) {
                    navController.navigate("camera/$categoryName/$dayIndex/${questionIndex}")
                },
                    colorFilter = if (completedQuestion) null else ColorFilter.colorMatrix(ColorMatrix().apply {
                setToSaturation(0.1f)
            })
        )
    }
}
