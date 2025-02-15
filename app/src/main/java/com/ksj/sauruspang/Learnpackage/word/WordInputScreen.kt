package com.ksj.sauruspang.Learnpackage.word

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.*
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.Learnpackage.ScoreViewModel
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R
import com.ksj.sauruspang.util.DialogCorrect
import com.ksj.sauruspang.util.DialogRetry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordInputScreen(
    navController: NavController,
    categoryName: String,
    dayIndex: Int,
    questionIndex: Int,
    tts: TextToSpeech?,
    viewModel: ProfileViewmodel,
    scoreViewModel: ScoreViewModel
) {
    var hitNumber by remember { mutableIntStateOf(0) }
    val inkManager = remember { InkManager() }
    var recognizedText by remember { mutableStateOf("Recognition Result: ") }
    var isModelDownloaded by remember { mutableStateOf(false) }
    val remoteModelManager = RemoteModelManager.getInstance()
    val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        ?: throw IllegalStateException("No model found for the given language tag")
    val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
    var recognizedList by remember { mutableStateOf(listOf<String>()) }

    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    val targetWord = question.english.uppercase()

    var showCorrectDialog by remember { mutableStateOf(false) }
    var showRetryDialog by remember { mutableStateOf(false) }
    var wrongLettersInfo by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var isCorrect by remember { mutableStateOf(false) }
    var incorrectAnswerCount by remember { mutableIntStateOf(0) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val nextRoute = if (questionIndex == questions.lastIndex) {
        "quiz/$categoryName/$dayIndex/$questionIndex"
    } else {
        "learn/$categoryName/$dayIndex/${questionIndex + 1}"
    }

    LaunchedEffect(Unit) {
        downloadModel(model, remoteModelManager) { success ->
            isModelDownloaded = success
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.confetti_wallpaper),
            contentDescription = " ",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "button to stagescreen",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("stage/$categoryName")
                        }
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.image_backarrow),
                    contentDescription = "previous question",
                    modifier = Modifier
                        .size(130.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            if (questionIndex > 0) {
                                navController.navigate("learn/$categoryName/$dayIndex/${questionIndex - 1}")
                            } else {
                                navController.navigate("learn/$categoryName/$dayIndex/0")
                            }
                        }
                )

                Box(
                    modifier = Modifier
                        .width(600.dp)
                        .height(250.dp)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset -> inkManager.startStroke(offset) },
                                onDrag = { change, _ -> inkManager.addPointToStroke(change.position) },
                                onDragEnd = { inkManager.endStroke() }
                            )
                        },
                ) {
                    Box {
                        Image(
                            painter = painterResource(question.imageId),
                            contentDescription = "Question Image",
                            modifier = Modifier
                                .size(300.dp)
                                .align(Alignment.CenterStart)
                                .offset(x = screenWidth * -0.1f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(450.dp)
                            .align(Alignment.CenterEnd)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.real_sketchbook),
                            contentDescription = "Sketchbook",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )

                        Text(
                            text = question.english,
                            style = TextStyle(
                                fontSize = 90.sp, fontWeight = FontWeight.Bold,
                                color = Color.Black.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    val redrawTrigger = inkManager.shouldRedraw
                    Canvas(modifier = Modifier.size(600.dp)) {
                        drawPath(inkManager.path, Color.Red, style = Stroke(width = 25f))
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.image_frontarrow),
                    contentDescription = "next question",
                    modifier = Modifier
                        .size(130.dp)
                        .align(Alignment.CenterVertically)
                        .clickable(enabled = isCorrect) {
                            if (questionIndex == questions.size - 1) {
                                navController.navigate("quiz/$categoryName/$dayIndex/0")
                            } else if (questionIndex in 1..<hitNumber) {
                                navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}")
                            } else {
                                navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}") {
                                    popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = false }
                                }
                            }
                        },
                    colorFilter = if (isCorrect) null else ColorFilter.tint(Color.Gray)
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                FilledTonalButton(
                    onClick = { inkManager.clearCanvas() },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp)
                        .padding(top = 10.dp),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(
                        "다시쓰기",
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }

                Spacer(modifier = Modifier.size(5.dp))
                FilledTonalButton(
                    onClick = {
                        if (isModelDownloaded) {
                            coroutineScope.launch {
                                val result = withContext(Dispatchers.IO) {
                                    inkManager.recognizeInk().uppercase()
                                }
                                recognizedText = result

                                val recognizedSplit = result.split(",").take(2)
                                if (recognizedSplit.size < 2) {
                                    recognizedText = "인식 결과가 부족합니다. 다시 시도해 주세요."
                                    return@launch
                                }
                                recognizedList = recognizedSplit
                                Log.e("recognizedList", recognizedList.toString())

                                val candidate1 = recognizedList[0]
                                val candidate2 = recognizedList[1]

                                val mismatchedIndexes1 = compareWords(targetWord, candidate1)
                                val mismatchedIndexes2 = compareWords(targetWord, candidate2)

                                if ((candidate1.length == targetWord.length && mismatchedIndexes1.isEmpty()) ||
                                    (candidate2.length == targetWord.length && mismatchedIndexes2.isEmpty())
                                ) {
                                    recognizedText = "정답입니다."
                                    showCorrectDialog = true
                                    isCorrect = true
                                    hitNumber++
                                } else {
                                    wrongLettersInfo = "틀린 글자: " +
                                            "${if (mismatchedIndexes1.isNotEmpty()) targetWord[mismatchedIndexes1[0]] else "?"}, " +
                                            "${if (mismatchedIndexes2.isNotEmpty()) targetWord[mismatchedIndexes2[0]] else "?"}"
                                    recognizedText = wrongLettersInfo
                                    showRetryDialog = true
                                    incorrectAnswerCount++
                                }
                            }
                        } else {
                            recognizedText = "Model is not yet downloaded. Please try again later."
                        }
                    },
                    modifier = Modifier
                        .width(120.dp)
                        .height(50.dp)
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(
                        "정답확인",
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(0.5f))
            }

            if (incorrectAnswerCount >= 3) {
                Button(onClick = {
                    navController.navigate(nextRoute)
                    incorrectAnswerCount = 0
                }) { Text("넘어가기") }
            }
        }

        if (showCorrectDialog) {
            DialogCorrect(
                scoreViewModel = scoreViewModel,
                onDismiss = { showCorrectDialog = false }
            )
        }

        if (showRetryDialog) {
            DialogRetry(
                wrongLetters = wrongLettersInfo,
                onDismiss = { showRetryDialog = false },
                onRetry = {
                    inkManager.clearCanvas()
                    showRetryDialog = false
                }
            )
        }
    }
}

private fun downloadModel(
    model: DigitalInkRecognitionModel,
    remoteModelManager: RemoteModelManager,
    onDownloadComplete: (Boolean) -> Unit
) {
    remoteModelManager.download(model, DownloadConditions.Builder().build())
        .addOnSuccessListener {
            Log.i("ModelDownload", "Model downloaded successfully")
            onDownloadComplete(true)
        }
        .addOnFailureListener { e: Exception ->
            Log.e("ModelDownload", "Error while downloading model: $e")
            onDownloadComplete(false)
        }
}

class InkManager {
    var path = Path()
    var shouldRedraw by mutableStateOf(false)

    private var inkBuilder = Ink.builder()
    private var strokeBuilder: Ink.Stroke.Builder? = null
    private val recognizer = createDigitalInkRecognizer()

    fun startStroke(offset: Offset) {
        strokeBuilder = Ink.Stroke.builder().apply {
            addPoint(Ink.Point.create(offset.x, offset.y, System.currentTimeMillis()))
        }
        path.moveTo(offset.x, offset.y)
        shouldRedraw = !shouldRedraw
    }

    fun addPointToStroke(offset: Offset) {
        strokeBuilder?.addPoint(Ink.Point.create(offset.x, offset.y, System.currentTimeMillis()))
        path.lineTo(offset.x, offset.y)
        shouldRedraw = !shouldRedraw
    }

    fun endStroke() {
        strokeBuilder?.let {
            inkBuilder.addStroke(it.build())
        }
        strokeBuilder = null
    }

    suspend fun recognizeInk(): String {
        val ink = inkBuilder.build()
        return try {
            val result = recognizer.recognize(ink).await()
            result.candidates.joinToString(", ") { it.text }
        } catch (e: Exception) {
            "Recognition failed: Please try again."
        }
    }

    fun clearCanvas() {
        path = Path()
        inkBuilder = Ink.builder()
        shouldRedraw = !shouldRedraw
    }
}

fun createDigitalInkRecognizer(): DigitalInkRecognizer {
    val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        ?: throw IllegalStateException("No model found for the given language tag")

    val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
    return DigitalInkRecognition.getClient(DigitalInkRecognizerOptions.builder(model).build())
}

fun compareWords(targetWord: String, recognizedWord: String): List<Int> {
    val mismatchedIndexes = mutableListOf<Int>()
    val length = minOf(targetWord.length, recognizedWord.length)

    for (i in 0 until length) {
        if (targetWord[i] != recognizedWord[i]) {
            mismatchedIndexes.add(i)
        }
    }

    return mismatchedIndexes
}