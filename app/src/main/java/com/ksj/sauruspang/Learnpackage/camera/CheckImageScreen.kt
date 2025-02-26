package com.ksj.sauruspang.Learnpackage.camera

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ksj.sauruspang.R
import com.ksj.sauruspang.flaskSever.ImageInput
import com.ksj.sauruspang.flaskSever.ImagePrediction
import com.ksj.sauruspang.util.LoadingDialog
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
        // capturedImage가 더미 이미지면 카메라 프리뷰 화면을 보여줌
        if (capturedImage.value == viewModel.dummyBitmpa) {
            CameraPreviewScreen { bitmap ->
                viewModel.setCapturedImage(bitmap) // ViewModel에 저장
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(R.drawable.wallpaper_learnscreen),
                    contentDescription = "배경 이미지",
                    contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
                    modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
                )
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(5f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(3f)
                                .padding(20.dp)
                        ) {
                            // 로딩 다이얼로그가 활성화되어 있으면 화면 상단에 표시합니다.
                            if (isLoading) {
                                LoadingDialog(message = viewModel.answerWord)
                            }
                            Image(
                                bitmap = capturedImage.value!!.asImageBitmap(),
                                contentDescription = "Captured Image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(10.dp, color = Color(0xff85C4CA))
                            )

                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 40.dp)
                                .weight(1f)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.check_answer_txt),
                                contentDescription = "정답 확인",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            )
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            )
                            Image(painter = painterResource(R.drawable.check_answer_yes_btn),
                                contentDescription = "yes",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(2f)
                                    .clickable {
                                        // 로딩 상태 true로 설정
                                        isLoading = true
                                        ImagePrediction.uploadImageToServer(
                                            context = context,
                                            imageInput = ImageInput.BitmapInput(bitmapCapturedImage),
                                            selectedModel = findCategoryName
                                        ) { updatedList ->
                                            if (viewModel.answerWord
                                                    .lowercase()
                                                    .trim() in updatedList
                                            ) {
                                                SelectViewModelList(
                                                    sharedRouteViewModel,
                                                    viewModel,
                                                    capturedImage
                                                )
                                                viewModel.isCorrect = true
                                                Log.d("isCorrect", "정답: ${viewModel.isCorrect}")
                                            } else {
                                                viewModel.isCorrect = false
                                                Log.d("isCorrect", "오답: ${viewModel.isCorrect}")
                                            }
                                            // 로딩 상태 false로 전환 후 메인 스레드에서 화면 전환
                                            coroutineScope.launch {
                                                isLoading = false
                                                navController.navigate("answer")
                                            }
                                            Log.e("isCorrect", "최종: ${viewModel.isCorrect}")

                                        }
                                    }
                            )
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            )
                            Image(
                                painter = painterResource(R.drawable.check_answer_no_btn),
                                contentDescription = "yes",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(2f)
                                    .clickable { capturedImage.value = viewModel.dummyBitmpa }
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
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
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("has_permission", value)
    editor.apply()
}

fun getPermission(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("has_permission", false) // 기본값은 false
}