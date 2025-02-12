package com.ksj.sauruspang.Learnpackage.word

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
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
//    val recognizedList = mutableListOf<String>()
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

    var nextRoute = if (questionIndex == questions.lastIndex) {
        "quiz/$categoryName/$dayIndex/$questionIndex"
    } else {
        "learn/$categoryName/$dayIndex/${questionIndex + 1}"
    }

    // 모델 다운로드 실행
    LaunchedEffect(Unit) {
        downloadModel(model, remoteModelManager) { success ->
            isModelDownloaded = success
        }
    }

//    Box(modifier = Modifier.fillMaxSize()) {
//        Image(
//            painter = painterResource(id = R.drawable.question_wallpaper),
//            contentDescription = " ",
//            contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
//            modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
//        )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDD4AA))
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
                Image(
                    painter = painterResource(R.drawable.real_sketchbook),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize() // 화면에 맞게 꽉 채우기
                )
                Text(
                    text = question.english,
                    style = TextStyle(
                        fontSize = 120.sp, fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
                val redrawTrigger = inkManager.shouldRedraw  // 변경 감지
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawPath(inkManager.path, Color.Red, style = Stroke(width = 25f))
                }
            }

            // 다음 문제 넘어가기 (정답 시 활성화)
            Image(
                painter = painterResource(id = R.drawable.image_frontarrow),
                //  id = if (isCorrect) R.drawable.image_frontarrow else R.drawable.frontnull
                contentDescription = "next question",
                modifier = Modifier
                    .size(130.dp)
                    .align(Alignment.CenterVertically)
                    .clickable(enabled = isCorrect) {
                        if (questionIndex == questions.size - 1) {
                            // Navigate to the first question of the quiz screen
                            navController.navigate("quiz/$categoryName/$dayIndex/0")
                        } else if (questionIndex in 1..<hitNumber) {
                            // Navigate to the previous learn screen
                            navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}")
                        } else {
                            // Navigate to the next learn screen
                            navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}") {
                                popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = false }
                            }
                        }
                    },
                colorFilter = if (isCorrect) null else ColorFilter.tint(Color.Gray)

            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(question.imageId),
                contentDescription = "Question Image",
                modifier = Modifier
                    .size(140.dp)
            )
//            Text(
//                text = recognizedText,
//                style = TextStyle(
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                ),
//                modifier = Modifier.padding(10.dp)
//            )
            FilledTonalButton(
                onClick = {
                    inkManager.clearCanvas()
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp),
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
            // 정답 확인 버튼
            FilledTonalButton(
                onClick = {
                    if (isModelDownloaded) {
                        coroutineScope.launch {
                            // recognizedText와 리스트 초기화
                            val result = withContext(Dispatchers.IO) {
                                inkManager.recognizeInk().uppercase()
                            }
                            // Main 스레드에서 상태 업데이트
                            recognizedText = result

                            // 안전하게 결과 분리
                            val recognizedSplit = result.split(",").take(2)
                            if (recognizedSplit.size < 2) {
                                // 인식 결과가 부족할 경우 추가 처리
                                recognizedText = "인식 결과가 부족합니다. 다시 시도해 주세요."
                                return@launch
                            }
                            recognizedList = recognizedSplit
                            Log.e("recognizedList", recognizedList.toString())

                            val candidate1 = recognizedList[0]
                            val candidate2 = recognizedList[1]

                            val mismatchedIndexes1 = compareWords(targetWord, candidate1)
                            val mismatchedIndexes2 = compareWords(targetWord, candidate2)

                            // 두 후보가 targetWord와 길이가 같고 모두 일치해야 정답으로 판단
                            // 정답 조건식 변경 가능 (candidate1 == targetWord || candidate2 == targetWord)
                            if ((candidate1.length == targetWord.length && mismatchedIndexes1.isEmpty()) ||
                                (candidate2.length == targetWord.length && mismatchedIndexes2.isEmpty())
                            ) {
                                recognizedText = "정답입니다."  // TODO 로깅 텍스트
                                showCorrectDialog = true
                                isCorrect = true
                                hitNumber++
                            } else {
                                // 틀린 글자 정보를 안전하게 생성 (단, 후보가 targetWord보다 짧은 경우 대비)
                                wrongLettersInfo = "틀린 글자: " +
                                        "${if (mismatchedIndexes1.isNotEmpty()) targetWord[mismatchedIndexes1[0]] else "?"}, " +
                                        "${if (mismatchedIndexes2.isNotEmpty()) targetWord[mismatchedIndexes2[0]] else "?"}"
                                recognizedText = wrongLettersInfo  // TODO 로깅 텍스트
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
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(2.dp, Color.Black), // Black outline

            ) {
                Text(
                    "정답확인",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                    )
                )
            }



            if (incorrectAnswerCount >= 3) {
                Button(onClick = {
                    navController.navigate(nextRoute)
                    incorrectAnswerCount = 0
                }) { Text("넘어가기") }
            }
        }

        // 최상위 레벨에서 상태에 따라 다이얼로그 표시
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
                    // 다시쓰기 동작 수행 (예: 캔버스 초기화)
                    inkManager.clearCanvas()
                    //     recognizedText = "Recognition Result: "
                    showRetryDialog = false
                }
            )
        }
    }
    // }
}


// 모델 다운로드 함수
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