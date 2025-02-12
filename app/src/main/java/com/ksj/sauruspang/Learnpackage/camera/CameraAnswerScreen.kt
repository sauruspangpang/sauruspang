package com.ksj.sauruspang.Learnpackage.camera

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    // 단일 상태 변수로 다이얼로그 표시 여부를 제어합니다.
    var showDialog by remember { mutableStateOf(false) }
    // 화면이 처음 구성될 때 다이얼로그를 표시하도록 설정합니다.
    LaunchedEffect(Unit) {
        showDialog = true
    }

    // showDialog가 true일 때, viewModel.isCorrect 값에 따라 적절한 다이얼로그 호출
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
                            // Navigate to the LearnScreen of the same question index
                            navController.navigate(sharedFront) {
                                popUpTo(sharedPopUp) { inclusive = false }
                            }
                        } else {
                            // Navigate to the LearnScreen of the previous question
                            if (questionIndex > 0) {
                                navController.navigate(sharedBack) {
                                    popUpTo(sharedPopUp) { inclusive = false }
                                }
                            }
                            clickCount = 0 // Reset click count after navigating back
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

//            val nextRoute = TODO()

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
                    .align(Alignment.BottomStart) // Move button to bottom end
                    .size(width = 200.dp, height = 60.dp), // Bigger button
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBE5FF))
            ) {
                Text("다시 찍기")
            }

            Button(
                //  enabled = (retryCount != 0 && (questionIndex ==questions.size - 1)),
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
                    .align(Alignment.BottomEnd) // Move button to bottom end
                    .size(width = 200.dp, height = 60.dp), // Bigger button
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBE5FF))
            ) {
                Text("넘어가기")
            }

        }
    }
}