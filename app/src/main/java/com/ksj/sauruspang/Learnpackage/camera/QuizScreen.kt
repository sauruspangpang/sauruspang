package com.ksj.sauruspang.Learnpackage.camera

import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ksj.sauruspang.R
import com.ksj.sauruspang.ui.theme.SauruspangTheme
import com.ksj.sauruspang.util.DialogCorrect
import com.ksj.sauruspang.util.DialogRetry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    categoryName: String,
    dayIndex: Int,
    questionIndex: Int,
    viewModel: ProfileViewmodel
) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    var progress by remember { mutableFloatStateOf(0.2f) } // Example progress (50%)

    val questionId = "$categoryName-$dayIndex-$questionIndex"
    val solvedQuestion by remember { derivedStateOf { viewModel.isQuizSolved(questionId) } }

    var showCorrectDialog by remember { mutableStateOf(false) }
    var showRetryDialog by remember { mutableStateOf(false) }

    val answerOptions = remember { questions.map { it.english }.shuffled() }

    if (showCorrectDialog) {
        DialogCorrect(
            message = "정답입니다.",
            onDismiss = { showCorrectDialog = false }
        )
    }
    if (showRetryDialog) {
        DialogRetry(
            onDismiss = { showRetryDialog = false },
            onRetry = {
                // 다시쓰기 동작 수행 (예: 캔버스 초기화)
                // recognizedText = "Recognition Result: "
                showRetryDialog = false
            }
        )
    }
    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()
            .background(Color(0xFFFDD4AA))

    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow),
            contentDescription = "button to stagescreen",
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    category?.name?.let { categoryName ->
                        navController.navigate("stage/$categoryName")
                    }
                }
        )
        Image(
            painter = painterResource(id = R.drawable.image_backarrow),
            contentDescription = "previous question",
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterStart)
                .clickable {
                    navController.navigate(
                        if (questionIndex > 0) {
                            "quiz/$categoryName/$dayIndex/${questionIndex - 1}"
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
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Spacer(modifier = Modifier.size(30.dp))
                    Image(
                        painter = painterResource(id = question.imageId),
                        contentDescription = "question image",
                        modifier = Modifier
                            .size(200.dp)

                    )
                    Text(
                        question.korean,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 45.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.size(50.dp))
                Column(
                    modifier = Modifier
                        .offset(x = 30.dp, y = (-20).dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Spacer(modifier = Modifier.size(50.dp))

                    answerOptions.forEach { answer ->
                        Button(
                            onClick = {
                                if (answer == question.english) {
                                    viewModel.markQuizAsSolved(questionId)
                                    showCorrectDialog = true
                                } else {
                                    showRetryDialog = true
                                }


                            },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(4.dp)
                        ) {
                            Text(answer, fontSize = 24.sp)
                        }
                    }
                }

            }

        }

        Image(
            painter = painterResource(id = R.drawable.image_frontarrow),
            contentDescription = "next question",
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterEnd)
//                    .clickable(enabled = questionIndex < questions.size - 1)
//                    {
//                        navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}") {
//                            popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = false }
//                        }
//
//                    }
                .clickable(enabled = solvedQuestion) {
                    if (questionIndex == questions.size - 1) {
                        navController.navigate("congrats")

                    } else {
                        navController.navigate("quiz/$categoryName/$dayIndex/${questionIndex + 1}")
                    }
                },

            colorFilter = if (solvedQuestion) null else ColorFilter.tint(Color.Gray)
        )
    }

}
