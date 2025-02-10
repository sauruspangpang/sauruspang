package com.ksj.sauruspang.Learnpackage.camera

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ksj.sauruspang.R

@Composable
fun CameraAnswerScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel(),
    sharedRouteViewModel: SharedRouteViewModel = viewModel()
) {
    val capturedImage = viewModel.capturedImage
    val sharedvModel = sharedRouteViewModel.sharedValue
    BackHandler {
        navController.popBackStack(sharedvModel.toString(), true)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.background)),
    ) {
        Row {
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
            Box(modifier = Modifier.weight(1f)){
                CapturedImage(capturedImage)
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
}