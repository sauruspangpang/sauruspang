package com.ksj.sauruspang.Learnpackage.camera

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R
import com.ksj.sauruspang.util.LearnCorrect
import com.ksj.sauruspang.util.LearnRetry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    categoryName: String,
    dayIndex: Int,
    questionIndex: Int,
    viewModel: ProfileViewmodel,
) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    var progress by remember { mutableFloatStateOf(0.2f) }

    val questionId = "$categoryName-$dayIndex-$questionIndex"
    // 정답 여부를 viewModel에서 가져옴
    val solvedQuestion by remember { derivedStateOf { viewModel.isQuizSolved(questionId) } }

    var showCorrectDialog by remember { mutableStateOf(false) }
    var showRetryDialog by remember { mutableStateOf(false) }

    // 정답 보기 옵션을 섞어서 표시
    val answerOptions = remember { questions.map { it.english }.shuffled() }

    if (showCorrectDialog) {
        LearnCorrect(
            onDismiss = { showCorrectDialog = false }
        )
    }
    if (showRetryDialog) {
        LearnRetry(
            onDismiss = { showRetryDialog = false },
            onRetry = { showRetryDialog = false }
        )
    }
    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper_learnscreen),
            contentDescription = "background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Image(
            painter = painterResource(id = R.drawable.icon_backtochooseda),
            contentDescription = "button to stagescreen",
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp)
                .clickable {
                    category?.name?.let { navController.navigate("stage/$it") }
                }
        )
        Image(
            painter = painterResource(id = R.drawable.icon_arrow_left),
            contentDescription = "previous question",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable {
                    navController.navigate(
                        if (questionIndex > 0) {
                            "quiz/$categoryName/$dayIndex/${questionIndex - 1}"
                        } else if (categoryName in listOf("직업")) {
                            "WordInput/$categoryName/$dayIndex/$questionIndex"
                        } else {
                            "camera/$categoryName/$dayIndex/${questions.size - 1}"
                        }
                    )
                }
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(700.dp)
                .align(Alignment.Center)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-10).dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(30.dp))
                    Image(
                        painter = painterResource(id = question.imageId),
                        contentDescription = "question image",
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = question.korean,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 45.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.size(50.dp))
                Column(
                    modifier = Modifier.offset(x = 30.dp, y = (-20).dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Spacer(modifier = Modifier.size(50.dp))
                    answerOptions.forEach { answer ->

                        Box() {
                            Image(
                                painter = painterResource(R.drawable.image_choosewordbutton),
                                contentDescription = "button",
                                modifier = Modifier.clickable {
                                    if (answer == question.english) {
                                        // 정답이면 바로 퀴즈 완료 처리 및 점수 5점 추가
                                        viewModel.markQuizAsSolved(questionId)
                                        val currentScore =
                                            viewModel.profiles.getOrNull(viewModel.selectedProfileIndex.value)?.score
                                                ?: 0
                                        viewModel.updateScore(currentScore + 5)
                                        showCorrectDialog = true
                                    } else {
                                        showRetryDialog = true
                                    }
                                })
                            Text(
                                text = answer,
                                fontSize = 24.sp,
                                color = Color(0xFFf0ffee),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Center)

                            )
                        }
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.icon_arrow_right),
            contentDescription = "next question",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(enabled = solvedQuestion) {
                    if (questionIndex == questions.size - 1) {
                        navController.navigate("congrats/${categoryName}")
                    } else {
                        navController.navigate("quiz/$categoryName/$dayIndex/${questionIndex + 1}")
                    }
                },
            colorFilter = if (solvedQuestion) null else ColorFilter.colorMatrix(ColorMatrix().apply {
                setToSaturation(0.1f)
            })
        )
    }
}
