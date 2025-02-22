package com.ksj.sauruspang.Learnpackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@Composable
fun StageScreen(navController: NavController, categoryName: String, viewModel: ProfileViewmodel, scoreViewModel: ScoreViewModel) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.day_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_home),
                    contentDescription = "button to stagescreen",
                    modifier = Modifier
                        .size(80.dp)
                        .offset(y = (-5).dp)
                        .clickable {
                            navController.popBackStack("home",false)
                        }
                )
                ProfileBox(scoreViewModel =scoreViewModel, viewModel = viewModel)
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
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}



@Composable
fun ZigzagRow(days: List<QuizDay>, categoryName: String, navController: NavController) {
    val totalDays = CategoryDayManager.getDay(categoryName) // 현재 카테고리의 활성화된 최대 day 가져오기

    Row(
        modifier = Modifier
            .padding(30.dp)
    ) {
        for (i in 0 until days.size) { // days.size를 기준으로 반복
            val isActive = i < totalDays // 현재 day가 활성화된 상태인지 확인

            DayBox(
                dayIndex = i, // 현재 인덱스를 dayIndex로 전달
                isTop = i % 2 == 0, // 박스를 위쪽에 배치할지 아래쪽에 배치할지 결정
                categoryName = categoryName, // 카테고리 이름 전달
                navController = navController, // NavController 전달
                isActive = isActive // 활성화 여부 전달
            )
        }
    }
}


@Composable
fun DayBox(dayIndex: Int, isTop: Boolean, categoryName: String, navController: NavController, isActive: Boolean) {
    val totalDays = CategoryDayManager.getDay(categoryName) // 현재 활성화된 Day 가져오기
    val isActive = dayIndex < totalDays // 현재 day가 활성화된 상태인지 확인

    Box(
        modifier = Modifier
            .offset(y = if (isTop) (-20).dp else 80.dp)
            .size(width = 140.dp, height = 90.dp)
            .alpha(if (isActive) 1f else 0.3f) // 비활성화된 Day는 투명하게 처리
            .clickable(enabled = isActive) { // 비활성화된 Day는 클릭 방지
                navController.navigate("learn/$categoryName/$dayIndex")
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_whiteboard),
            contentDescription = "null",
        )
        Text(
            "Day ${dayIndex + 1}",
            style = TextStyle(fontSize = 40.sp)
        )
    }
}

