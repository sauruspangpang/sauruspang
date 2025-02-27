package com.ksj.sauruspang.Learnpackage.camera

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R
import com.ksj.sauruspang.util.CaptureCorrect
import com.ksj.sauruspang.util.CaptureRetry

@Composable
fun CameraAnswerScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel(),
    sharedRouteViewModel: SharedRouteViewModel = viewModel(),
    profileViewmodel: ProfileViewmodel // 프로필별 도감 저장용 뷰모델
) {
    val capturedImage = viewModel.capturedImage
    val sharedvModel = sharedRouteViewModel.sharedValue
    val category = sharedRouteViewModel.sharedCategory
    var clickCount = sharedRouteViewModel.sharedClickCount
    val sharedFront = sharedRouteViewModel.sharedFront
    val sharedQuizStart = sharedRouteViewModel.sharedQuizStart
    val sharedPopUp = sharedRouteViewModel.sharedPopUp
    val questionIndex = sharedRouteViewModel.sharedQuestionIndex
    val question = sharedRouteViewModel.sharedQuestion
    val questions = sharedRouteViewModel.sharedQuestions
    val sharedBack = sharedRouteViewModel.sharedBack

    var correct by remember { mutableStateOf(false) }
    var retryCount by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        showDialog = true
    }

    if (showDialog) {
        if (viewModel.isCorrect) {
            capturedImage.value?.let { bitmap ->
                // 정답이면 도감에 업데이트 (기존 프로필 데이터는 그대로 유지)
                profileViewmodel.updateCatalogEntry(viewModel.answerWord, bitmap)
            }
            Toast.makeText(
                navController.context,
                "도감에 들어갔어요!",
                Toast.LENGTH_SHORT
            ).show()
            CaptureCorrect(
                onDismiss = {
                    viewModel.isCorrect = false
                    showDialog = false
                    correct = true
                }
            )
        } else {
            CaptureRetry(
                onDismiss = {
                    showDialog = false
                    retryCount++
                    correct = false
                }
            )
        }
    }

    BackHandler {
        navController.popBackStack(sharedvModel.toString(), true)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper_learnscreen),
            contentDescription = "background",
            modifier = Modifier.matchParentSize()
        )

        Image(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = "previous question",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable {
                    if (clickCount == 1) {
                        navController.navigate(sharedFront) {
                            popUpTo(sharedPopUp) { inclusive = false }
                        }
                    } else {
                        if (questionIndex > 0) {
                            navController.navigate(sharedBack) {
                                popUpTo(sharedPopUp) { inclusive = false }
                            }
                        }
                        clickCount = 0
                    }
                }
        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clickable {
                    navController.navigate("camerax")
                    sharedRouteViewModel.sharedCategory = category
                }
        ) {
            CapturedImage(capturedImage, modifier = Modifier.matchParentSize().align(Alignment.BottomCenter).offset(y = (5).dp))
            Image(
                painter = painterResource(id = R.drawable.image_frame),
                contentDescription = "camera",
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(1.2f)
            )
        }
        Text(
            text = question.english,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 20.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 75.sp
            )
        )
        Image(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = "next question",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(enabled = correct) {
                    if (questionIndex == questions.size - 1) {
                        navController.navigate(sharedQuizStart) {
                            popUpTo(sharedQuizStart) { inclusive = false }
                        }
                    } else {
                        navController.navigate(sharedFront) {
                            popUpTo(sharedPopUp) { inclusive = false }
                        }
                    }
                    retryCount = 0
                    correct = false
                },
            colorFilter = if (correct) null else ColorFilter.colorMatrix(
                ColorMatrix().apply {
                    setToSaturation(0.1f)
                })
        )
        Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_camera_retry),
                contentDescription = "다시 찍기",
                modifier = Modifier.clickable(enabled = (retryCount != 0)) {
                    capturedImage.value = viewModel.dummyBitmpa
                    navController.navigate("camerax")
                })
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(R.drawable.icon_camera_skip),
                contentDescription = "넘어가기",
                modifier = Modifier
                    .clickable(enabled = !correct) {
                        if (questionIndex == questions.size - 1) {
                            navController.navigate(sharedQuizStart) {
                                popUpTo(sharedQuizStart) { inclusive = false }
                            }
                        } else {
                            navController.navigate(sharedFront) {
                                popUpTo(sharedPopUp) { inclusive = false }
                            }
                        }
                        retryCount = 0
                        correct = false
                    }
            )
        }

    }
}
