package com.ksj.sauruspang.Learnpackage.camera

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.Learnpackage.QuizQuestion
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

var findCategoryName = ""

@Composable
fun CameraScreen(
    navController: NavController,
    categoryName: String,
    dayIndex: Int,
    questionIndex: Int,
    viewModel: ProfileViewmodel,
    camViewModel: CameraViewModel,
    sharedRouteViewModel: SharedRouteViewModel
) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    var clickCount by remember { mutableIntStateOf(0) }
    val findCategory = findCategoryByQuestion(question)
    val categoryname = findCategory?.javaClass?.simpleName ?: "Unknown"
    var hasPermission by remember { mutableStateOf(false) }
    findCategoryName = categoryname

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
                        clickCount++
                        if (clickCount == 1) {
                            // Navigate to the LearnScreen of the same question index
                            navController.navigate("learn/$categoryName/$dayIndex/$questionIndex") {
                                popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = false }
                            }
                        } else {
                            // Navigate to the LearnScreen of the previous question
                            if (questionIndex > 0) {
                                navController.navigate("learn/$categoryName/$dayIndex/${questionIndex - 1}") {
                                    popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = false }
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
                        sharedRouteViewModel.sharedClickCount = clickCount
                        sharedRouteViewModel.sharedFront =
                            "learn/$categoryName/$dayIndex/${questionIndex + 1}"
                        sharedRouteViewModel.sharedPopUp = "learn/$categoryName/$dayIndex/0"
                        sharedRouteViewModel.sharedQuestionIndex = questionIndex
                        sharedRouteViewModel.sharedQuestion = question
                        sharedRouteViewModel.sharedQuestions = questions
                        sharedRouteViewModel.sharedBack =
                            "learn/$categoryName/$dayIndex/${questionIndex - 1}"
                        sharedRouteViewModel.sharedCategoryName = categoryName

                    }

            ) {
                Text(categoryname, modifier = Modifier.align(Alignment.Center))
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
            camViewModel.answerWord = question.english
            Image(
                painter = painterResource(id = R.drawable.frontnull),
                contentDescription = "next question",
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterEnd)
                    .clickable(enabled = questionIndex < questions.size - 1)
                    {
                        navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}") {
                            popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = false }
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

@Composable
fun BackgroundScreen(category: QuizCategory?, navController: NavController) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.arrow),
                contentDescription = "뒤로 가기 버튼",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp)
                    .weight(1f)
                    .clickable {
                        category?.name?.let { categoryName ->
                            navController.navigate("stage/$categoryName")
                        }
                    }
            )
            Text(
                "사진을 찍어보세요",
                fontSize = 50.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(5f)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.background_fruit_2),
                contentDescription = "오른쪽 배경",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.6f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.background_fruit_1),
                contentDescription = "왼쪽 배경",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.8f)
            )
        }
    }
}

fun findCategoryByQuestion(question: QuizQuestion): QuizCategory? {
    return QuizCategory.allCategories.firstOrNull { category ->
        category.days.any { day ->
            day.questions.contains(question)
        }
    }
}