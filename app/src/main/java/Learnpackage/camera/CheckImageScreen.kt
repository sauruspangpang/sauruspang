package Learnpackage.camera

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ksj.sauruspang.R

// 이미지 표시하는 Compose 함수
@Composable
fun ShowCameraPreviewScreen(navController: NavController, viewModel: CameraViewModel = viewModel()) {
    val capturedImage = viewModel.capturedImage

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
                modifier = Modifier.fillMaxSize()
                    .background(color = colorResource(R.color.background)),
                contentAlignment = Alignment.BottomCenter
            ) {
                CapturedImage(capturedImage)
                Row (modifier = Modifier.height(60.dp)){
                    //버튼들 박스로 만들고 텍스트로 기능 적어두고 클릭어블로 기능넣기
                    Button(
                        onClick = { capturedImage.value = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RectangleShape,
                        modifier = Modifier.weight(1f)
                            .fillMaxSize()
                    ) {
                        Text("다시 촬영", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(onClick = {
                        navController.navigate("answer")
                    },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        shape = RectangleShape,
                        modifier = Modifier.weight(1f)
                            .fillMaxSize()) {
                        Text("정답 확인", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CapturedImage(capturedImage: MutableState<Bitmap?>){
    Image(
        bitmap = capturedImage.value!!.asImageBitmap(),
        contentDescription = "Captured Image",
        modifier = Modifier.fillMaxSize()
    )
}