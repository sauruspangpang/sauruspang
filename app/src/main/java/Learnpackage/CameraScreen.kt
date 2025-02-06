package Learnpackage

import ProfilePackage.ProfileViewmodel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import  androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    navController: NavController,
    categoryName: String,
    dayIndex: Int,
    questionIndex: Int,
    viewModel: ProfileViewmodel
) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    var clickCount by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDD4AA))
    ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()


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
                    .align(Alignment.TopStart) // Align image to the left
            )
            Text(
                text = "사진을 찍어보세요",
                modifier = Modifier
                    .align(Alignment.TopCenter),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp
                )
            )
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
                    .size(height=200.dp,width=450.dp)
                    .align(Alignment.Center)
                    .offset(y = (-15).dp)
                    .background(Color.LightGray)

            ){
                Text("camera", modifier = Modifier.align(Alignment.Center))
            }
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
                    .clickable(enabled = questionIndex < questions.size - 1)
                    {
                        navController.navigate("learn/$categoryName/$dayIndex/${questionIndex + 1}") {
                            popUpTo("learn/$categoryName/$dayIndex/0") { inclusive = false }
                        }

                    }
            )
            Button(onClick = {

            },
                    modifier = Modifier
                    .align(Alignment.BottomEnd) // Move button to bottom end
                .size(width = 200.dp, height = 60.dp), // Bigger button
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDBE5FF))
            ){
                Text("넘어가기")
            }

        }
    }

}