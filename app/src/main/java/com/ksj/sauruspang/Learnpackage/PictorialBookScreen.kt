package com.ksj.sauruspang.Learnpackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@Composable
fun PictorialBookScreen(
    navController: NavController,
//    categoryName: String,
//    dayIndex: Int,
//    questionIndex: Int,
    viewModel: ProfileViewmodel
) {
//    val category = QuizCategory.allCategories.find { it.name == categoryName }
//    val questions = category?.days?.get(dayIndex)?.questions ?: emptyList()
//    val question = questions[questionIndex]
//    var progress by remember { mutableFloatStateOf(0.2f) } // Example progress (50%)

    Column(modifier = Modifier.fillMaxSize().background(color = Color(0xFFFDD4AA))) {
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
                        navController.navigate("profile")
                    }
            )

        }
    }
}