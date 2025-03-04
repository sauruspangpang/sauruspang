package com.ksj.sauruspang.Learnpackage.camera

import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.R
import com.ksj.sauruspang.ui.theme.SauruspangTheme

class DummyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SauruspangTheme {
                DummyScreen(
                    navController = rememberNavController(),
                    categoryName = "과일",
                    dayIndex = 0,
                    questionIndex = 0
                )
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
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

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
                    Image(painter = painterResource(id = R.drawable.icon_backtochooseda),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(top = 10.dp, start = 10.dp)
                            .clickable {
                                category?.name?.let { categoryName ->
                                    navController.navigate("stage/$categoryName")
                                }
                            })

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            // .height(20.dp)
                            .height(screenHeight * 0.04f)
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
            Image(painter = painterResource(id = R.drawable.icon_arrow_left),
                contentDescription = "previous question",
                modifier = Modifier
                    //  .size(140.dp)
                    .size(screenWidth * 0.15f)
                    .align(Alignment.CenterStart)
                    .offset(x = screenWidth * 0.03f)
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

                    Image(
                        painter = painterResource(id = question.imageId),
                        contentDescription = "question image",
                        modifier = Modifier.size(screenWidth * 0.2f)
                    )
                    Spacer(modifier = Modifier.height(screenHeight * 0.02f))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            question.korean,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold, fontSize = 45.sp
                            )
                        )
                        Image(
                            painter = painterResource(id = R.drawable.icon_readword),
                            contentDescription = "listen button",
                            modifier = Modifier
                                .size(screenWidth * 0.07f)
                                .padding(start = screenWidth * 0.02f)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            question.english,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold, fontSize = 45.sp
                            )
                        )
                        Image(
                            painter = painterResource(id = R.drawable.icon_readword),
                            contentDescription = "listen button",
                            modifier = Modifier
                                .size(screenWidth * 0.07f)
                                .padding(start = screenWidth * 0.02f)

                        )
                    }

                }
                Spacer(modifier = Modifier.width(screenWidth * 0.08f))
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(screenHeight * 0.23f))
                    Box(
                        modifier = Modifier
                            .size(screenWidth * 0.12f)
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(16.dp)
                            ) // Increased shadow for more visibility

                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF77E4D2), // Bright turquoise
                                        Color(0xFF4ECDC4)  // Slightly darker shade
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        // Glossy effect overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.4f), // Shiny highlight
                                            Color.Transparent
                                        ),
                                        center = Offset(30f, 20f), // Light source effect
                                        radius = 120f
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )
                        Image(
                            painter = painterResource(id = R.drawable.speakbutton),
                            contentDescription = "Speak button",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentScale = ContentScale.Fit // Ensures it fills the box
                        )
                    }
                    Spacer(modifier = Modifier.height(screenHeight * 0.05f))

                    Row {
                        repeat(3) { index ->
                            Image(
                                painter = painterResource(id = question.imageId),
                                contentDescription = "listen button",
                                // modifier = Modifier.size(50.dp)
                                modifier = Modifier.size(screenWidth * 0.055f).padding(horizontal = 5.dp),
                                alpha = if (index < correctCount) 1.0f else 0.4f
                            )
                            //index 0 = image1, index1 = image2, index2 = image3
                        }
                    }


                }
            }

            Image(painter = painterResource(id = R.drawable.icon_arrow_right),
                contentDescription = "next question",
                modifier = Modifier
                    .size(screenWidth * 0.155f)
                    .align(Alignment.CenterEnd)
                    .offset(x = -(screenWidth * 0.03f))

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
fun DummyScreenPreview() {
    SauruspangTheme {
        DummyScreen(rememberNavController(), "과일과 야채", dayIndex = 0, questionIndex = 1)
    }
}
