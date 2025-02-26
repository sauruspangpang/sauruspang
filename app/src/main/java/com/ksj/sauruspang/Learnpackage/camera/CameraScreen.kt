package com.ksj.sauruspang.Learnpackage.camera

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
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
    sharedRouteViewModel: SharedRouteViewModel,
) {
    val context = LocalContext.current
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    var clickCount by remember { mutableIntStateOf(0) }
    val findCategory = findCategoryByQuestion(question)
    val categoryname = findCategory?.javaClass?.simpleName ?: "Unknown"
    findCategoryName = categoryname


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper_learnscreen),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .zIndex(-1f)
        )

        Image(
            painter = painterResource(R.drawable.icon_backtochooseda),
            contentDescription = "뒤로 가기 버튼",
            modifier = Modifier
                .clickable {
                    category?.name?.let { categoryName ->
                        navController.navigate("stage/$categoryName")
                    }
                }
        )
        camViewModel.clearImage()

        Image(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = "previous question",
            modifier = Modifier
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
        Text(
            text = "${question.korean}을(를) 찍어보세요",
            modifier = Modifier
                .align(Alignment.TopCenter),
            fontWeight = FontWeight.Bold,
            fontSize = 75.sp

        )
        Box(
            modifier = Modifier
                .align(Alignment.Center)

        ) {
            Image(
                painter = painterResource(R.drawable.icon_camera),
                contentDescription = "camera icon",
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        val camPermission =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (camPermission != PackageManager.PERMISSION_GRANTED) {
                            Toast
                                .makeText(context, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT)
                                .show()
                        } else
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
                        sharedRouteViewModel.sharedQuizStart = "quiz/$categoryName/$dayIndex/0"
                    }
            )
            Image(
                painter = painterResource(R.drawable.image_frame), contentDescription = "frame",
                modifier = Modifier.align(Alignment.Center)
            )
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
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = "next question",
            modifier = Modifier
                .align(Alignment.CenterEnd),
            colorFilter = ColorFilter.colorMatrix(
                ColorMatrix().apply {
                    setToSaturation(0.1f)
                })

        )
        Image(
            painter = painterResource(R.drawable.icon_camera_skip),
            contentDescription = "",
            modifier = Modifier.clickable { }
                .align(Alignment.TopEnd))

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
    }
}

fun findCategoryByQuestion(question: QuizQuestion): QuizCategory? {
    return QuizCategory.allCategories.firstOrNull { category ->
        category.days.any { day ->
            day.questions.contains(question)
        }
    }
}