package com.ksj.sauruspang.Learnpackage.camera

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R
import com.ksj.sauruspang.ui.theme.SauruspangTheme
import java.util.Locale

class DummyActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            SauruspangTheme {
                DummyScreen(navController = rememberNavController(), categoryName = "과일", dayIndex = 0, questionIndex = 0)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DummyScreen(
    navController: NavController,
    categoryName: String,
    dayIndex: Int,
    questionIndex: Int,
){
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
    val question = questions[questionIndex]
    var progress by remember { mutableFloatStateOf(0.2f) }
    var correctCount by remember { mutableIntStateOf(0) }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(painter = painterResource(id = R.drawable.image_backhome),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                category?.name?.let { categoryName ->
                                    navController.navigate("stage/$categoryName")
                                }
                            })

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
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFDD4AA))

        ) {
            Image(painter = painterResource(id = R.drawable.image_backarrow),
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
                    })
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Image(painter = painterResource(id = question.imageId),
                        contentDescription = "question image",
                        modifier = Modifier
                            .size(180.dp)



                    )
                    Text(
                        question.korean,
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .offset(y=-(20).dp),

                        style = TextStyle(
                            fontWeight = FontWeight.Bold, fontSize = 50.sp
                        )
                    )
                    Text(
                        question.english,

                        style = TextStyle(
                            fontWeight = FontWeight.Bold, fontSize = 60.sp
                        )
                    )
                    Image(painter = painterResource(id = R.drawable.listen),
                        contentDescription = "listen button",
                        modifier = Modifier
                            .size(30.dp))



                }
                Spacer(modifier = Modifier.size(80.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(70.dp))
                    Box(
                        modifier = Modifier
                            .size(65.dp)
                            .clip(RoundedCornerShape(8.dp)) // Rounded corners
                            .background(Color(0xFF77E4D2))
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp), clip = false)

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.speakbutton),
                            contentDescription = "Speak button",
                            modifier = Modifier
                                .size(60.dp)
                                .padding(8.dp) // Inner padding,
                        )
                    }
                    Text("detected:", fontSize = 20.sp)
                    Row() {
                        Row {
                            repeat(3) { index ->
                                Image(
                                    painter = painterResource(id = question.imageId),
                                    contentDescription = "listen button",
                                    modifier = Modifier.size(30.dp),
                                    alpha = if (index < correctCount) 1.0f else 0.4f
                                )
                                //index 0 = image1, index1 = image2, index2 = image3
                            }
                        }

                    }

                }

            }
            Image(painter = painterResource(id = R.drawable.image_frontarrow),
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
                    .clickable { navController.navigate("camera/$categoryName/$dayIndex/${questionIndex}") })
        }
    }





}

@Preview(showBackground = true, widthDp = 900, heightDp = 400)
@Composable
fun DummyScreenPreview(){
    SauruspangTheme {
        DummyScreen(rememberNavController(),  "과일과 야채", dayIndex = 0, questionIndex = 0)
    }
}
