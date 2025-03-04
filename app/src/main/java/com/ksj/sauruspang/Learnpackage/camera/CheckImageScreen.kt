package com.ksj.sauruspang.Learnpackage.camera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
//import com.ksj.sauruspang.flaskSever.ImagePrediction
import com.ksj.sauruspang.util.LoadingDialog
import com.ksj.sauruspang.util.ImagePrediction
import kotlinx.coroutines.launch

// 이미지 표시하는 Compose 함수
@Composable
fun ShowCameraPreviewScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel(),
    resultListViewModel: DetectedResultListViewModel = viewModel(),
    sharedRouteViewModel: SharedRouteViewModel = viewModel()
) {
    val capturedImage = viewModel.capturedImage
    var predictionResultListState by remember { mutableStateOf(listOf<String>()) }
    val context = LocalContext.current
    val bitmapCapturedImage = getBitmapFromState(capturedImage)
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (capturedImage.value == viewModel.dummyBitmpa) {
            CameraPreviewScreen { bitmap ->
                viewModel.setCapturedImage(bitmap) // ViewModel에 저장
            }
        } else {
            if (isLoading) {
                LoadingDialog(message = viewModel.answerWord)
            }

            LaunchedEffect(Unit) { // isLoading을 트리거로 사용하지 않음
                if (isLoading) return@LaunchedEffect // 중복 요청 방지

                isLoading = true // 로딩 시작
//                ImagePrediction.uploadImageToServer(
//                    context = context,
//                    imageInput = ImageInput.BitmapInput(bitmapCapturedImage),
//                    selectedModel = findCategoryName
//                ) { updatedList ->
//                    viewModel.isCorrect = viewModel.answerWord.lowercase().trim() in updatedList
//
//                    if (viewModel.isCorrect) {
//                        SelectViewModelList(sharedRouteViewModel, viewModel, capturedImage)
//                    }
//
//                    Log.d("isCorrect", "결과: ${viewModel.isCorrect}")
//
//                    coroutineScope.launch {
//                        isLoading = false // 서버 응답 후 로딩 종료
//                        navController.navigate("answer")
//                    }
//                }
                // (1) 로컬 ONNX 추론 실행
                val updatedList = if (bitmapCapturedImage != null) {
                    runLocalOnnxModel(
                        context = context,
                        bitmap = bitmapCapturedImage,
                        categoryName = findCategoryName,
                        answerWord = viewModel.answerWord
                    )
                } else emptyList()
                Log.d("OnnxDebug", "ONNX 최종 결과 리스트: $updatedList")

                // (2) 정답 여부 확인
                viewModel.isCorrect = viewModel.answerWord.lowercase().trim() in updatedList
                Log.d("OnnxDebug", "isCorrect: ${viewModel.isCorrect}")

                if (viewModel.isCorrect) {
                    SelectViewModelList(sharedRouteViewModel, viewModel, capturedImage)
                }
                Log.d("isCorrect", "결과: ${viewModel.isCorrect}")

                // (3) 네비게이션 이동
                coroutineScope.launch {
                    isLoading = false
                    navController.navigate("answer")
                }
            }
        }
    }
}

/**
 * 분류/탐지 로직을 내부에서 분기하여, 파이썬 서버에서 받던 String 리스트(클래스명들)와 유사하게 반환.
 * 예: ["apple", "banana"], ["dog"], ...
 */
private fun runLocalOnnxModel(
    context: Context,
    bitmap: Bitmap,
    categoryName: String,
    answerWord: String
): List<String> {
    Log.d("OnnxDebug", "로컬 모델 실행: category=$categoryName, answerWord=$answerWord")

    val detectionModelName = "fruits_detection_11n_v10.onnx"  // 예시
    val classificationModelName = "color_cls_11n_v10.onnx"  // 예시

    val resultList = when (categoryName.lowercase()) {
        "colors" -> {
            val results = ImagePrediction.classificationONNX(
                context = context,
                bitmap = bitmap,
                modelFileName = classificationModelName,
                useCenterRegion = true,
                threshold = 0.2f,
                topK = 5
            ).map { it.first }

            Log.d("OnnxDebug", "분류 결과: $results")
            results
        }

        "fruits", "animals" -> {
            val boxes = ImagePrediction.detectionONNX(
                context = context,
                bitmap = bitmap,
                modelFileName = detectionModelName,
                confThreshold = 0.1f
            )

            val mappedLabels = boxes.map { box -> "class_${box.classId}" }.distinct()

            Log.d("OnnxDebug", "탐지 결과: $mappedLabels")
            mappedLabels
        }

        else -> {
            Log.e("OnnxDebug", "알 수 없는 카테고리: $categoryName")
            emptyList()
        }
    }

    Log.d("OnnxDebug", "최종 반환값: $resultList")
    return resultList
}

fun SelectViewModelList(
    sharedRouteViewModel: SharedRouteViewModel,
    viewModel: CameraViewModel,
    capturedImage: MutableState<Bitmap?>
) {
    when (sharedRouteViewModel.sharedCategoryName) {
        "과일과 야채" -> if (viewModel.answerWord !in viewModel.correctFruitWordList) {
            viewModel.isCorrect = true
            viewModel.correctFruitWordList.add(viewModel.answerWord)
            viewModel.correctFruitImageList.add(
                Bitmap.createBitmap(
                    capturedImage.value!!
                )
            )
        }

        "동물" -> {
            if (viewModel.answerWord !in viewModel.correctAnimalWordList) {
                viewModel.isCorrect = true
                viewModel.correctAnimalWordList.add(viewModel.answerWord)
                viewModel.correctAnimalImageList.add(
                    Bitmap.createBitmap(
                        capturedImage.value!!
                    )
                )
            }
        }

        "색" -> {
            if (viewModel.answerWord !in viewModel.correctColorWordList) {
                viewModel.isCorrect = true
                viewModel.correctColorWordList.add(viewModel.answerWord)
                viewModel.correctColorImageList.add(
                    Bitmap.createBitmap(
                        capturedImage.value!!
                    )
                )
            }
        }

        "직업" -> {
            if (viewModel.answerWord !in viewModel.correctJobWordList) {
                viewModel.isCorrect = true
                viewModel.correctJobWordList.add(viewModel.answerWord)
                viewModel.correctJobImageList.add(
                    Bitmap.createBitmap(
                        capturedImage.value!!
                    )
                )
            }
        }
    }

}

@Composable
fun CapturedImage(capturedImage: MutableState<Bitmap?>, modifier: Modifier) {
    Image(
        bitmap = capturedImage.value!!.asImageBitmap(),
        contentDescription = "Captured Image",
        modifier = modifier
    )
}

fun getBitmapFromState(bitmapState: MutableState<Bitmap?>): Bitmap? {
    return bitmapState.value  // nullable 상태 그대로 반환
}


fun savePermission(context: Context, value: Boolean) {
    val sharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("has_permission", value)
    editor.apply()
}

fun getPermission(context: Context): Boolean {
    val sharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("has_permission", false) // 기본값은 false
}