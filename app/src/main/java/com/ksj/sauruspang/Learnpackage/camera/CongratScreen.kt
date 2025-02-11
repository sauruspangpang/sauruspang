package com.ksj.sauruspang.Learnpackage.camera

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.ksj.sauruspang.Learnpackage.CategoryDayManager
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import android.util.Log

@Composable
fun CongratScreen(navController: NavController, viewModel: ProfileViewmodel, categoryName: String) {
    // 로그를 추가하여 호출을 추적
    Log.e("CongratScreen", "CategoryDayManager.incrementDay($categoryName) called")

    // LaunchedEffect를 사용하여 컴포저블이 처음 구성될 때만 호출되도록 설정
    LaunchedEffect(key1 = categoryName) {
        CategoryDayManager.incrementDay(categoryName)
        val currentDay = CategoryDayManager.getDay(categoryName)
        Log.e("CongratScreen", "CategoryDayManager.getDay($categoryName): $currentDay")

        // 네비게이션을 한 번만 호출
        navController.navigate("stage/$categoryName") {
            popUpTo("congrats/$categoryName") { inclusive = true }
        }
    }

    // 현재 카테고리의 day 증가
    Text("Congratulation! You have completed the day!")
}