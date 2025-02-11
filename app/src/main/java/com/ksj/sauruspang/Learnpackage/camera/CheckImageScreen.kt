package com.ksj.sauruspang.Learnpackage.camera

import android.Manifest
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ksj.sauruspang.R
import com.ksj.sauruspang.flaskSever.ImageInput
import com.ksj.sauruspang.flaskSever.ImagePrediction

// 이미지 표시하는 Compose 함수
@Composable
fun ShowCameraPreviewScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel(),
    resultListViewModel: DetectedResultListViewModel = viewModel()
) {
    val capturedImage = viewModel.capturedImage
    val context = LocalContext.current
    val bitmapCapturedImage = getBitmapFromState(capturedImage)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (capturedImage.value == null) {
            CameraPreviewScreen { bitmap ->
                viewModel.setCapturedImage(bitmap) // ViewModel에 저장
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(R.color.background)),
                contentAlignment = Alignment.BottomCenter
            ) {
                CapturedImage(capturedImage)
                Row(modifier = Modifier.height(60.dp)) {
                    //버튼들 박스로 만들고 텍스트로 기능 적어두고 클릭어블로 기능넣기
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .background(color = Color.Red)
                            .clickable { capturedImage.value = null }
                    ) {
                        Text("다시 촬영", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                ImagePrediction.uploadImageToServer(
                                    context = context,
                                    imageInput = ImageInput.BitmapInput(bitmapCapturedImage),
                                    selectedModel = findCategoryName
                                ) { updatedList ->
                                    if (viewModel.answerWord
                                            .lowercase()
                                            .trim() in updatedList
                                    ) {
                                        if (viewModel.answerWord !in viewModel.correctWordList) {
                                            viewModel.isCorrect = true
                                            viewModel.correctWordList.add(viewModel.answerWord)
                                            viewModel.correctImageList.add(
                                                Bitmap.createBitmap(
                                                    capturedImage.value!!
                                                )
                                            )
                                        }
                                    }
                                }
                                navController.navigate("answer")
                            }
                            .background(color = Color.Green)
                            .fillMaxSize()) {
                        Text("정답 확인", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CapturedImage(capturedImage: MutableState<Bitmap?>) {
    Image(
        bitmap = capturedImage.value!!.asImageBitmap(),
        contentDescription = "Captured Image",
        modifier = Modifier.fillMaxSize()
    )
}

fun getBitmapFromState(bitmapState: MutableState<Bitmap?>): Bitmap? {
    return bitmapState.value  // nullable 상태 그대로 반환
}

@Composable
fun RequestCameraPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                Toast.makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}