@file:Suppress("DEPRECATION")

package com.ksj.sauruspang.Learnpackage.camera

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ksj.sauruspang.Learnpackage.CategoryDayManager
import com.ksj.sauruspang.Learnpackage.QuizCategory
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@Composable
fun CongratScreen(navController: NavController, viewModel: ProfileViewmodel, categoryName: String) {
    // 프로필에 저장된 현재 활성 day를 가져와 +1 한 후 DB에 업데이트
    LaunchedEffect(key1 = categoryName) {
        val currentDay = viewModel.getActiveDay(categoryName)
        val newDay = currentDay + 1
        viewModel.updateCategoryDayStatus(categoryName, newDay)
        Log.e("CongratScreen", "Updated $categoryName day to: $newDay")
    }
    val category = QuizCategory.allCategories.find { it.name == categoryName }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.wallpaper_congratulations),
            contentDescription = "conftti",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .width(250.dp)
                .wrapContentHeight()
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(painter = painterResource(id = R.drawable.icon_backtochooseda),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .clickable {
                        category?.name?.let { categoryName ->
                            navController.navigate("stage/$categoryName")
                        }
                    }
            )
            Box(modifier = Modifier.fillMaxSize()) {
                CongratAnimation()
            }
        }
    }
}
@Composable
fun CongratAnimation() {

        val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))
        val progress2 by animateLottieCompositionAsState(
            composition = composition2,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition2,
            progress = progress2,
            modifier = Modifier.fillMaxSize()
        )

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.komacongrat))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.fillMaxSize()
        )


}
