package com.ksj.sauruspang.Learnpackage

import android.util.Log
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.R

@Composable
fun StageScreen(navController: NavController, categoryName: String, viewModel: ProfileViewmodel) {
    val quizCategory = QuizCategory.allCategories.find { it.name == categoryName }
    val days = quizCategory?.days ?: emptyList()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.day_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_backhome),
                    contentDescription = "button to stagescreen",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("home")
                        }
                )
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .offset(y = (-30).dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.image_woodboard),
                        contentDescription = "",
                    )
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ellipse_1),
                            contentDescription = "",
                            Modifier.scale(0.8f)
                        )
                        Column(
                            modifier = Modifier
                                .padding(bottom = 20.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                "Hello 박민준",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.image_starpoint),
                                    contentDescription = "",
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "245",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.image_photobook),
                    contentDescription = "",
                    modifier = Modifier
                        .size(70.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .horizontalScroll(rememberScrollState())
            ) {
                Column {
                    Button(onClick = {
                        viewModel.increaseDayCount("역할")
                        Log.d("Profile", "Day Count for $categoryName: ${viewModel.profiles.find { it.quizCategory == categoryName }?.fruitsDayCount}")
                        Log.d("Profile", viewModel.profiles.toString())
                    }) {
                        Text("데이 증가")
                    }
                    Column {
                        ZigzagRow(days, categoryName, navController, viewModel)
                    }
                }
            }
        }
    }
}




@Composable
fun ZigzagRow(
    days: List<QuizDay>,
    categoryName: String,
    navController: NavController,
    viewModel: ProfileViewmodel
) {
    // 상태 관리: List<Profile>을 UI에서 사용
    val profiles = viewModel.profiles // 이미 State로 관리되고 있으므로 collectAsState() 필요 없음

    // 프로필을 찾고 dayCount를 가져옴
    val profile = profiles.find { it.quizCategory == categoryName }
    val dayCount = when (categoryName) {
        "과일과 야채" -> profile?.fruitsDayCount ?: 1
        "동물" -> profile?.animalsDayCount ?: 1
        "색" -> profile?.colorsDayCount ?: 1
        "직업" -> profile?.jobsDayCount ?: 1
        else -> 1
    }

    Row(
        modifier = Modifier.padding(30.dp)
    ) {
        days.take(dayCount).forEachIndexed { index, day ->
            DayBox(
                dayIndex = day.dayNumber - 1,
                isTop = index % 2 == 0,
                categoryName = categoryName,
                navController = navController
            )
        }
    }
}
@Composable
fun DayBox(
    dayIndex: Int,
    isTop: Boolean,
    categoryName: String,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(if (isTop) Color.LightGray else Color.Gray)
            .clickable {
                // Click action to navigate
                navController.navigate("quiz_detail/$categoryName/$dayIndex")
            }
            .size(100.dp)
    ) {
        Text(
            text = "Day ${dayIndex + 1}",
            modifier = Modifier
                .align(if (isTop) Alignment.TopCenter else Alignment.BottomCenter)
                .padding(8.dp)
        )
    }
}