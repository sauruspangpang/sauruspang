package com.ksj.sauruspang

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
    NavHost(navController = navController, if (ProfileViewmodel().profiles.isEmpty()) "main" else "profile",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable("main") {
            MainScreen(navController, viewmodel)
        }
        composable("profile") {
            ProfilePage(navController, viewmodel)
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