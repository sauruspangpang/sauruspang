package com.ksj.sauruspang.Learnpackage.word

import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
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
import com.ksj.sauruspang.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordInputScreen(
    navController: NavController,
    categoryName: String,
    dayIndex: Int,
    questionIndex: Int,
    viewModel: ProfileViewmodel
) {
    val inkManager = remember { InkManager() }
    var recognizedText by remember { mutableStateOf("Recognition Result: ") }
    var isModelDownloaded by remember { mutableStateOf(false) }
    val remoteModelManager = RemoteModelManager.getInstance()
    val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        ?: throw IllegalStateException("No model found for the given language tag")
    val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
    val recognizedList = mutableListOf<String>()
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    val targetWord = question.english.uppercase()

    // 모델 다운로드 실행
    LaunchedEffect(Unit) {
        downloadModel(model, remoteModelManager) { success ->
            isModelDownloaded = success
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
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
                        navController.popBackStack()
                    }
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "previous question",
                    modifier = Modifier
                        .size(140.dp)
                        .clickable(enabled = questionIndex > 0) {
                            if (questionIndex > 0) {
                                navController.navigate("camera/$categoryName/$dayIndex/${questionIndex - 1}")
                            } else {
                                navController.popBackStack()
                            }
                        }
                )
            Box(
                modifier = Modifier
                    .width(600.dp)
                    .height(200.dp)
                    .background(Color.LightGray)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset -> inkManager.startStroke(offset) },
                            onDrag = { change, _ -> inkManager.addPointToStroke(change.position) },
                            onDragEnd = { inkManager.endStroke() }
                        )
                    },

                ) {
                Text(
                    text = "${question.english}",
                    style = TextStyle(
                        fontSize = 120.sp, fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
                val redrawTrigger = inkManager.shouldRedraw  // 변경 감지
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawPath(inkManager.path, Color.Black, style = Stroke(width = 5f))
                }
            }
            Image(
                painter = painterResource(id = R.drawable.frontnull),
                contentDescription = "next question",
                modifier = Modifier
                    .size(140.dp)
                    .clickable { navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(question.imageId),
                contentDescription = "next question",
                modifier = Modifier
                    .size(140.dp)
            )
            Button(
                onClick = {
                    if (isModelDownloaded) {
                        CoroutineScope(Dispatchers.IO).launch {
                            recognizedText = inkManager.recognizeInk().uppercase()
                            val recognizedSplit = recognizedText.split(",").take(2)
                            recognizedList.addAll(recognizedSplit)
                            Log.e("recognizedList", recognizedList.toString())

                            val mismatchedIndexes1 = compareWords(targetWord, recognizedList[0])
                            val mismatchedIndexes2 = compareWords(targetWord, recognizedList[1])

                            recognizedText =
                                if (mismatchedIndexes1.isEmpty() || mismatchedIndexes2.isEmpty()) {
                                    "정답입니다."
                                } else {
                                    "틀렸습니다."
                                }
                        }
                    } else {
                        recognizedText = "Model is not yet downloaded. Please try again later."
                    }
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
            ) {
                Text("정답확인")
            }
            Button(
                onClick = {
                    inkManager.clearCanvas()
                    recognizedText = "Recognition Result: "
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
            ) {
                Text("다시쓰기")
            }
        }

        // 결과 표시
        Text(
            text = recognizedText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

//모델 다운로드 함수
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


// InkManager 클래스 (실시간 그리기 지원)
class InkManager {
    var path = Path()  // 상태 변수 제거
    var shouldRedraw by mutableStateOf(false)  // Compose가 변경 감지하도록 추가

    private var inkBuilder = Ink.builder()
    private var strokeBuilder: Ink.Stroke.Builder? = null
    private val recognizer = createDigitalInkRecognizer()

    fun startStroke(offset: Offset) {
        strokeBuilder = Ink.Stroke.builder().apply {
            addPoint(Ink.Point.create(offset.x, offset.y, System.currentTimeMillis()))
        }
        path.moveTo(offset.x, offset.y)
        shouldRedraw = !shouldRedraw  // 화면을 다시 그리도록 상태 변경
    }

    fun addPointToStroke(offset: Offset) {
        strokeBuilder?.addPoint(Ink.Point.create(offset.x, offset.y, System.currentTimeMillis()))
        path.lineTo(offset.x, offset.y)
        shouldRedraw = !shouldRedraw  // 화면을 다시 그리도록 상태 변경
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
        shouldRedraw = !shouldRedraw  // 화면을 다시 그리도록 상태 변경
    }
}

// ML Kit Digital Ink Recognizer 생성
fun createDigitalInkRecognizer(): DigitalInkRecognizer {
    val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        ?: throw IllegalStateException("No model found for the given language tag")

    val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
    return DigitalInkRecognition.getClient(DigitalInkRecognizerOptions.builder(model).build())
}

// 글자 비교 함수
fun compareWords(targetWord: String, recognizedWord: String): List<Int> {
    val mismatchedIndexes = mutableListOf<Int>()
    val length = minOf(targetWord.length, recognizedWord.length)

    for (i in 0 until length) {
        if (targetWord[i] != recognizedWord[i]) {
            mismatchedIndexes.add(i)  // 틀린 인덱스를 추가
        }
    }

    return mismatchedIndexes
}
