package com.ksj.sauruspang.Learnpackage.camera

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel

@Composable
fun CongratScreen(navController: NavController, viewModel: ProfileViewmodel, categoryName: String) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    category?.incrementDayNumber()
    Text("Congratulations")
    navController.navigate("stage/$categoryName")
}