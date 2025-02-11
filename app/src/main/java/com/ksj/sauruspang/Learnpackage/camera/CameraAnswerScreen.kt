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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val category = sharedRouteViewModel.sharedCategory
    var clickCount = sharedRouteViewModel.sharedClickCount
    val sharedFront = sharedRouteViewModel.sharedFront
    val sharedPopUp = sharedRouteViewModel.sharedPopUp
    val questionIndex = sharedRouteViewModel.sharedQuestionIndex
    val question = sharedRouteViewModel.sharedQuestion
    val questions = sharedRouteViewModel.sharedQuestions
    val sharedBack = sharedRouteViewModel.sharedBack
    val categoryname = sharedRouteViewModel.sharedCategoryName
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
                painter = painterResource(id = R.drawable.back),
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

            Image(
                painter = painterResource(id = R.drawable.frontnull),
                contentDescription = "next question",
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterEnd)
                    .clickable(enabled = questionIndex < questions.size - 1)
                    {
                        navController.navigate(sharedFront) {
                            popUpTo(sharedPopUp) { inclusive = false }
                        }

                    }
            )
            Button(
                onClick = { /*todo*/ },
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Move button to bottom end
                    .size(width = 200.dp, height = 60.dp), // Bigger button
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBE5FF))
            ) {
                Text("넘어가기")
            }

        }
    }
}