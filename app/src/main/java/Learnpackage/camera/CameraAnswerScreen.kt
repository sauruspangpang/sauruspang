package Learnpackage.camera

import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ksj.sauruspang.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CameraAnswerScreen(navController: NavController, viewModel: CameraViewModel = viewModel()) {
    val capturedImage = viewModel.capturedImage
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.background)),
    ) {
        CapturedImage(capturedImage)
        Image(
            painter = painterResource(R.drawable.goback_btn),
            contentDescription = "뒤로 가기 버튼",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
                .weight(1f)
                .clickable { /*  todo  */ }
        )
        Column (modifier = Modifier.weight(1f)){
//            CapturedImage(capturedImage)
            Row {

            }
        }
        Image(
            painter = painterResource(R.drawable.retry_btn),
            contentDescription = "새로 고침 버튼",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
                .weight(1f)
                .clickable { /*  todo  */ }
        )
    }
}