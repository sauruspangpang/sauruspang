package com.ksj.sauruspang.Learnpackage

import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.R

@Composable
fun StageScreen(navController: NavController, categoryName: String, viewModel: ProfileViewmodel) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(R.drawable.day_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
            modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
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
                            .padding(horizontal = 30.dp), // 좌우 여백 추가
                        verticalAlignment = Alignment.CenterVertically // 내부 요소도 중앙 정렬
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ellipse_1),
                            contentDescription = "",
                            Modifier.scale(0.8f)
                        )
                        Column(
                            modifier = Modifier
                                .padding(bottom = 20.dp), // 내부 요소 패딩
                            verticalArrangement = Arrangement.SpaceBetween, // 내부 요소 정렬
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
                                verticalAlignment = Alignment.CenterVertically // 이미지와 텍스트를 같은 높이로 정렬
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.image_starpoint),
                                    contentDescription = "",
                                    modifier = Modifier.size(36.dp) // 원하는 크기로 조정
                                )
                                Spacer(modifier = Modifier.width(10.dp)) // 이미지와 숫자 사이 간격
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
                category?.days?.let { days ->
                    ZigzagRow(days, categoryName, navController)
                }
            }
        }
    }
}



@Composable
fun ZigzagRow(days: List<QuizDay>, categoryName: String, navController: NavController) {
    val totalDays = QuizCategory.DayNumberManager.dayNumber // DayNumberManager에서 업데이트된 dayNumber 가져오기
    Row(
        modifier = Modifier
            .padding(30.dp)
    ) {
        for (i in 0 until totalDays) {
            DayBox(
                dayIndex = i, // 현재 인덱스를 dayIndex로 전달
                isTop = i % 2 == 0, // 박스를 위쪽에 배치할지 아래쪽에 배치할지 결정
                categoryName = categoryName, // 카테고리 이름 전달
                navController = navController // NavController 전달
            )
        }
    }
}
@Composable
fun DayBox(dayIndex: Int, isTop: Boolean, categoryName: String, navController: NavController) {
    Box(
        modifier = Modifier
            .offset(y = if (isTop) (-20).dp else 80.dp)
            .size(width = 140.dp, height = 90.dp)
            .background(Color.White)
            .clickable {
                navController.navigate("learn/$categoryName/$dayIndex")
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Day ${dayIndex+1}",
            style = TextStyle(fontSize = 40.sp)
        )
    }
}