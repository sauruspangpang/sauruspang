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
import androidx.compose.foundation.layout.height
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
    Box(modifier = Modifier.fillMaxSize()) {
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
                    .padding(start = 30.dp),
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
                ProfileBox()
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
    Row(
        modifier = Modifier
            .padding(30.dp)

    ) {
        days.forEachIndexed { index, day ->
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
fun DayBox(dayIndex: Int, isTop: Boolean, categoryName: String, navController: NavController) {
    Box(
        modifier = Modifier
            .offset(y = if (isTop) (-20).dp else 80.dp)
            .size(width = 140.dp, height = 90.dp)
            .clickable {
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