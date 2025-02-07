package com.ksj.sauruspang

import Learnpackage.camera.CameraScreen
import Learnpackage.HomeScreen
import Learnpackage.camera.LearnScreen
import Learnpackage.camera.QuizScreen
import Learnpackage.StageScreen
import Learnpackage.word.WordInputScreen
import Learnpackage.word.WordQuizScreen
import ProfilePackage.MainScreen
import ProfilePackage.ProfilePage
import ProfilePackage.ProfileViewmodel
import android.os.Bundle
import android.speech.tts.TextToSpeech
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
import java.util.Locale

class MainActivity : ComponentActivity() {
    private lateinit var tts: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US // Set default language to English
            }
        }
        setContent {
            SauruspangTheme {
                NaySys(viewmodel = ProfileViewmodel(), tts)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown() // Release resources when the activity is destroyed
    }
}

@Composable
fun NaySys(viewmodel: ProfileViewmodel,tts: TextToSpeech) {
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
        composable("stage/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            StageScreen(navController, categoryName, viewmodel)
        }
        composable("learn/{categoryName}/{dayIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0

            // Navigate to the first question index (0) when the user reaches the day
            navController.navigate("learn/$categoryName/$dayIndex/0")
        }


        composable("learn/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            // Check if the category is not Fruits, Animals, or Colors
            if (categoryName !in listOf("과일", "동물", "색")) {
                WordQuizScreen(navController, categoryName, dayIndex, questionIndex, viewmodel)
            } else {
                LearnScreen(navController, categoryName, dayIndex, questionIndex, tts, viewmodel)
            }
        }

        composable("WordInput/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            WordInputScreen(navController, categoryName, dayIndex, questionIndex, viewmodel)
        }

        composable("camera/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            CameraScreen(navController, categoryName, dayIndex, questionIndex, viewmodel)
        }

    }
}

@Preview(widthDp = 1000, heightDp = 450, showBackground = true)
@Composable
fun GreetingPreview() {
    SauruspangTheme {
        LearnScreen(
            navController = rememberNavController(),
            categoryName = "과일",
            0,
            0,
            null,
            ProfileViewmodel()
        )
    }
}