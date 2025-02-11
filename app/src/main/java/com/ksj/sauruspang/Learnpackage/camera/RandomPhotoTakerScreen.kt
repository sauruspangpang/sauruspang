package com.ksj.sauruspang.Learnpackage.camera

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R
import com.ksj.sauruspang.flaskSever.ImageInput
import com.ksj.sauruspang.flaskSever.ImagePrediction

@Composable
fun RandomPhotoTakerScreen(
    navController: NavController,
    viewModel: ProfileViewmodel,
    camViewModel: CameraViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDD4AA))
    ) {
        var hasPermission by remember { mutableStateOf(false) }
        if (hasPermission) {
            CameraPreviewScreen { bitmap ->
                // 캡처된 이미지를 처리하는 코드
            }
        } else {
            RequestCameraPermission {
                hasPermission = true
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.background))
        ) {
            camViewModel.clearImage()
            Box(
                modifier = Modifier
                    .size(height = 200.dp, width = 450.dp)
                    .align(Alignment.Center)
                    .background(Color.LightGray)
                    .clickable {
                        navController.navigate("randomCamera")
                    }
            ) {
                Text("Open Camera", modifier = Modifier.align(Alignment.Center))
            }


            Image(
                painter = painterResource(id = R.drawable.image_backhome),
                contentDescription = "button to stagescreen",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopStart)
                    .clickable {
                        navController.navigate("home")
                    }
            )
            Text(
                "궁금한 물체의 사진을 찍어보세요",
                fontSize = 50.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun ShowRandomCameraPreviewScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel(),
    resultListViewModel: DetectedResultListViewModel = viewModel()
) {
    val capturedImage = viewModel.capturedImage
    var predictionResultListState by remember { mutableStateOf(listOf<String>()) }
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
                                /*TODO  chatGPT 물체 인식 확인 로직 코드 넣으시면 될거 같아용*/
                                navController.navigate("randomCameraAnswer")
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
fun CapturedImageRandom(capturedImage: MutableState<Bitmap?>) {
    val bitmap = capturedImage.value
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Captured Image",
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Text("No image captured yet.")
    }
}


@Composable
fun RandomCameraAnswerScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel()
) {
    val capturedImage = viewModel.capturedImage

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.background))
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_backhome),
            contentDescription = "button to stagescreen",
            modifier = Modifier
                .size(50.dp)
                .padding(10.dp)
                .align(Alignment.TopStart)
                .clickable { navController.navigate("randomPhotoTaker") }
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White)
                .align(Alignment.TopEnd)
                .padding(5.dp)
                .clickable {
                    viewModel.capturedImage.value = null

                    navController.popBackStack()
                }
        ){
            Text("다시 촬영")
        }
        Box(
            modifier = Modifier
                .size(height = 300.dp, width = 500.dp)
                .align(Alignment.TopCenter)
                .offset(y = (10).dp)
                .background(Color.LightGray)
                .clickable {
                    navController.navigate("randomCamera")
                }
        ) {
            CapturedImageRandom(capturedImage)
        }

    }
}