package com.ksj.sauruspang.Learnpackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                            navController.popBackStack("home", false)
                        }
                )
                ProfileBox(scoreViewModel = scoreViewModel, viewModel = viewModel)
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
    val totalDays = CategoryDayManager.getDay(categoryName)
    Row(modifier = Modifier.padding(30.dp)) {
        for (i in 0 until totalDays) {
            DayBox(
                dayIndex = i,
                isTop = i % 2 == 0,
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