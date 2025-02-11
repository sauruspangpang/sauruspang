package com.ksj.sauruspang.Learnpackage.camera

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel

@Composable
fun CongratScreen(navController: NavController, viewModel: ProfileViewmodel) {
    // dayNumber 증가
    QuizCategory.DayNumberManager.incrementDayNumber()

    Text("Congratulations")
}