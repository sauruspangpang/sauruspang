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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
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
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@Composable
fun StageScreen(navController: NavController, categoryName: String, viewModel: ProfileViewmodel) {
    // viewModel.getActiveDay(categoryName)를 사용하여 선택된 프로필의 활성화된 Day 정보를 가져옴
    val totalDays = viewModel.getActiveDay(categoryName)
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.wallpaper_chooseday),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Image(
            painter = painterResource(id = R.drawable.icon_changecategory),
            contentDescription = "button to stagescreen",
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp)
                .clickable {
                    navController.popBackStack("home", false)
                }
        )
        // 기존 ProfileBox 재사용 (Score 관련은 viewModel의 score 사용)
        ProfileBox(viewModel = viewModel, modifier = Modifier.align(Alignment.TopCenter))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .horizontalScroll(rememberScrollState())
        ) {
            // category?.days 대신 totalDays와 days 리스트를 활용 (QuizCategory를 그대로 사용)
            // QuizCategory.allCategories에서 categoryName에 해당하는 카테고리를 찾는다고 가정
            val category = QuizCategory.allCategories.find { it.name == categoryName }
            category?.days?.let { days ->
                ZigzagRow(days, categoryName, navController, viewModel)
            }
        }
    }
}

@Composable
fun ZigzagRow(days: List<QuizDay>, categoryName: String, navController: NavController, viewModel: ProfileViewmodel) {
    // 선택된 프로필의 활성화된 Day 수를 viewModel에서 가져옴
    val totalDays = viewModel.getActiveDay(categoryName)
    Row(
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        for (i in 0 until days.size) {
            // 현재 day가 활성화되었는지 viewModel의 값을 기준으로 확인
            val isActive = i < totalDays
            DayBox(
                dayIndex = i,
                categoryName = categoryName,
                navController = navController,
                isActive = isActive
            )
            Spacer(modifier = Modifier.width(50.dp))
        }
    }
}

@Composable
fun DayBox(dayIndex: Int, categoryName: String, navController: NavController, isActive: Boolean) {
    Box(
        modifier = Modifier
            .clickable(enabled = isActive) {
                navController.navigate("learn/$categoryName/$dayIndex")
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.card_day),
            contentDescription = "null",
            colorFilter = if (!isActive) {
                ColorFilter.colorMatrix(ColorMatrix().apply {
                    setToSaturation(0.1f)
                })
            } else null,
        )
        Text(
            "Day ${dayIndex + 1}",
            color = if (isActive) {Color.White} else {Color.LightGray},
            fontWeight = FontWeight.ExtraBold,
            fontSize = 48.sp,
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 35.dp, end = 30.dp)
        )
    }
}