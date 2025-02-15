package com.ksj.sauruspang.Learnpackage.camera

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ksj.sauruspang.Learnpackage.ScoreViewModel
import com.ksj.sauruspang.R
import com.ksj.sauruspang.util.CaptureCorrect
import com.ksj.sauruspang.util.CaptureRetry

@Composable
fun CameraAnswerScreen(
    navController: NavController,
    viewModel: CameraViewModel = viewModel(),
    sharedRouteViewModel: SharedRouteViewModel = viewModel(),
    scoreViewModel: ScoreViewModel
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
    val categoryname = sharedRouteViewModel.sharedCategoryName

    var correct by remember { mutableStateOf(false) }
    var retryCount by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showDialog = true
    }

    if (showDialog) {
        if (viewModel.isCorrect) {
            CaptureCorrect(
                scoreViewModel = scoreViewModel,
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
            .background(color = colorResource(R.color.background))
    ) {
        BackgroundScreen(category, navController)
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.image_backarrow),
                contentDescription = "previous question",
                modifier = Modifier
                    .size(140.dp)
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
                    .size(height = 200.dp, width = 450.dp)
                    .align(Alignment.Center)
                    .offset(y = (-15).dp)
                    .background(Color.LightGray)
                    .clickable {
                        navController.navigate("camerax")
                        sharedRouteViewModel.sharedCategory = category
                    }
            ) {
                CapturedImage(capturedImage)
            }
            Text(
                question.english,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = -(20).dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 75.sp
                )
            )
            Image(
                painter = painterResource(id = R.drawable.image_frontarrow),
                contentDescription = "next question",
                modifier = Modifier
                    .size(140.dp)
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
                colorFilter = if (correct) null else ColorFilter.tint(Color.Gray)
            )
            Button(
                enabled = (retryCount != 0),
                onClick = {
                    capturedImage.value = viewModel.dummyBitmpa
                    navController.navigate("camerax")
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(width = 200.dp, height = 60.dp)
            ) {
                Text("다시 찍기")
            }
            Button(
                enabled = !correct,
                onClick = {
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
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(width = 200.dp, height = 60.dp)
            ) {
                Text("넘어가기")
            }
        }
    }
}