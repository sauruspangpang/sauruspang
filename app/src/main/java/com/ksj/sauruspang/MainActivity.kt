package com.ksj.sauruspang

import Learnpackage.HomeScreen
import Learnpackage.LearnScreen
import Learnpackage.StageScreen
import Learnpackage.WordQuizScreen
import ProfilePackage.MainScreen
import ProfilePackage.ProfilePage
import ProfilePackage.ProfileViewmodel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ksj.sauruspang.ui.theme.SauruspangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SauruspangTheme {
                NaySys(viewmodel = ProfileViewmodel())
            }
        }
    }
}

@Composable
fun NaySys(viewmodel: ProfileViewmodel) {
    val navController = rememberNavController()
    NavHost(navController = navController,
        if (ProfileViewmodel().profiles.isEmpty()) "main" else "profile",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable("main") {
            MainScreen(navController, viewmodel)
        }
        composable("profile") {
            ProfilePage(navController, viewmodel)
        }
        composable("home") {
            HomeScreen(navController, viewmodel)
        }

        composable("stage/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            StageScreen(navController, viewmodel, category)
        }
        composable("learn/{category}/{day}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val day = backStackEntry.arguments?.getString("day") ?: ""
            if (category == "과일"
                || category == "색깔"
                || category == "동물"
                || category == "이동수단"
                || category == "학용품"
                || category == "옷"
            ) {
                LearnScreen(navController, category, day)
            } else WordQuizScreen(navController, category, day)
        }
    }
}


@Preview(widthDp = 1000, heightDp = 450, showBackground = true)
@Composable
fun GreetingPreview() {
    SauruspangTheme {
        MainScreen(navController = rememberNavController(), viewModel = ProfileViewmodel())
    }
}