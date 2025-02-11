package Learnpackage.camera

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ksj.sauruspang.Learnpackage.QuizCategory
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
    viewModel: ProfileViewmodel
) {

    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    var progress by remember { mutableFloatStateOf(0.2f) } // Example progress (50%)
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
            message = "정답입니다.",
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
    var hasPermission by remember { mutableStateOf(false) }

    speechRecognizer.setRecognitionListener(recognitionListener)
    RequestMicrophonePermission(onPermissionGranted = {
        hasPermission = true
    })
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(painter = painterResource(id = R.drawable.image_backhome),
                        contentDescription = "",
                        modifier = Modifier
                            .size(screenWidth * 0.07f)
                            .clickable {
                                category?.name?.let { categoryName ->
                                    navController.navigate("stage/$categoryName")
                                }
                            })

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(20.dp)
                            .align(Alignment.Center)
                    )
                }
            },

            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFFDD4AA)
            )
        )
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFDD4AA))

        ) {
            Image(painter = painterResource(id = R.drawable.image_backarrow),
                contentDescription = "previous question",
                modifier = Modifier
                    .size(screenWidth * 0.15f)
                    .align(Alignment.CenterStart)
                    .clickable(enabled = questionIndex > 0) {
                        if (questionIndex > 0) {
                            navController.navigate("camera/$categoryName/$dayIndex/${questionIndex - 1}")
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

                    ) {
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
                    horizontalAlignment = Alignment.CenterHorizontally
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
                            .clickable { speechRecognizer.startListening(speechIntent) }

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
                contentDescription = "next question",
                modifier = Modifier
                    .size(screenWidth * 0.155f)
                    .align(Alignment.CenterEnd)
                 //   .offset(x = -(screenWidth * 0.03f))
//                    .clickable(enabled = questionIndex < questions.size - 1)
//                    {
//                        navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}") {
//                            popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = false }
//                        }
//
//                    }
                    .clickable(enabled = completedQuestion)
                    { navController.navigate("camera/$categoryName/$dayIndex/${questionIndex}") },
                colorFilter = if (completedQuestion) null else ColorFilter.tint(Color.Gray)

            )


        }
    }
}


@Composable
fun RequestMicrophonePermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    var showRationale by remember { mutableStateOf(false) }

    // Permission launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            showRationale = true
        }
    }

// Check if the permission is already granted
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionGranted()
        } else {
            showRationale = true
        }
    }

    // Show rationale dialog if needed
    if (showRationale) {
        AlertDialog(onDismissRequest = { /* Handle dialog dismissal */ },
            title = { Text("Microphone Permission Required") },
            text = { Text("This app requires microphone access to function properly.") },
            confirmButton = {
                TextButton(onClick = {
                    requestPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                }) {
                    Text("Grant Permission")
                }
            },
            dismissButton = {
                TextButton(onClick = { /* Handle dismiss */ }) {
                    Text("Cancel")
                }
            })
    }
}
