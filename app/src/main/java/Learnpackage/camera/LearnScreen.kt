package Learnpackage.camera

import Learnpackage.QuizCategory
import ProfilePackage.ProfileViewmodel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    category?.name?.let { categoryName ->
                                        navController.navigate("stage/$categoryName")
                                    }
                                }
                        )

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(20.dp)
                                .align(Alignment.Center)
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFDD4AA)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFDD4AA))

        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "previous question",
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterStart)
                    .clickable(enabled = questionIndex > 0) {
                        if (questionIndex > 0) {
                            navController.navigate("camera/$categoryName/$dayIndex/${questionIndex - 1}")
                        } else {
                            navController.popBackStack()
                        }
                    }
            )
            Image(
                painter = painterResource(id = question.imageId),
                contentDescription = "question image",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)

            )
            Text(question.english,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y=-(20).dp),
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
//                    .clickable(enabled = questionIndex < questions.size - 1)
//                    {
//                        navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}") {
//                            popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = false }
//                        }
//
//                    }
                    .clickable { navController.navigate("camera/$categoryName/$dayIndex/${questionIndex}") }
            )
        }
    }

}