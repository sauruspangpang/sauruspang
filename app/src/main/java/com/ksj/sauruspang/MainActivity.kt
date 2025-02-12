package com.ksj.sauruspang

import Learnpackage.camera.LearnScreen
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ksj.sauruspang.Learnpackage.CategoryDayManager
import com.ksj.sauruspang.Learnpackage.HomeScreen
import com.ksj.sauruspang.Learnpackage.PictorialBookScreen
import com.ksj.sauruspang.Learnpackage.ScoreViewModel
import com.ksj.sauruspang.Learnpackage.StageScreen
import com.ksj.sauruspang.Learnpackage.camera.CameraAnswerScreen
import com.ksj.sauruspang.Learnpackage.camera.CameraScreen
import com.ksj.sauruspang.Learnpackage.camera.CameraViewModel
import com.ksj.sauruspang.Learnpackage.camera.CongratScreen
import com.ksj.sauruspang.Learnpackage.camera.DetectedResultListViewModel
import com.ksj.sauruspang.Learnpackage.camera.GPTCameraViewModel
import com.ksj.sauruspang.Learnpackage.camera.GPTRandomPhotoTakerScreen
import com.ksj.sauruspang.Learnpackage.camera.QuizScreen
import com.ksj.sauruspang.Learnpackage.camera.SharedRouteViewModel
import com.ksj.sauruspang.Learnpackage.camera.ShowCameraPreviewScreen
import com.ksj.sauruspang.Learnpackage.word.WordInputScreen
import com.ksj.sauruspang.Learnpackage.word.WordQuizScreen
import com.ksj.sauruspang.ProfilePackage.MainScreen
import com.ksj.sauruspang.ProfilePackage.ProfilePage
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
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
        val viewModel = ProfileViewmodel(application)
        setContent {
            val scoreViewModel: ScoreViewModel = viewModel()
            RequestPermissions()
            HideSystemBars()
            SauruspangTheme {
                NaySys(viewModel, tts, scoreViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown() // Release resources when the activity is destroyed
    }
}

@Composable
fun NaySys(viewmodel: ProfileViewmodel, tts: TextToSpeech, scoreViewModel: ScoreViewModel) {
    val navController = rememberNavController()
    val cameraViewModel: CameraViewModel = viewModel()
    val sharedRouteViewModel: SharedRouteViewModel = viewModel()
    val detectedResultListViewModel: DetectedResultListViewModel = viewModel()
    val gPTCameraViewModel: GPTCameraViewModel = viewModel()


    NavHost(navController = navController,
        startDestination = if (viewmodel.profiles.isEmpty()) "main" else "profile",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }) {
        composable("main") {
            MainScreen(navController, viewmodel)
        }
        composable("profile", enterTransition = {
            slideInHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                // Offsets the content by 1/3 of its width to the left, and slide towards right
                // Overwrites the default animation with tween for this slide animation.
                fullWidth / -1
            }
        }, exitTransition = {
            slideOutHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                // Offsets the content by 1/3 of its width to the left, and slide towards right
                // Overwrites the default animation with tween for this slide animation.
                fullWidth / 1
            }
        }) {
            ProfilePage(navController, viewmodel)
        }
        composable("home", enterTransition = {
            slideInHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                // Offsets the content by 1/3 of its width to the left, and slide towards right
                // Overwrites the default animation with tween for this slide animation.
                fullWidth / -1
            }
        }, exitTransition = {
            slideOutHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                // Offsets the content by 1/3 of its width to the left, and slide towards right
                // Overwrites the default animation with tween for this slide animation.
                fullWidth / 1
            }
        }) {
            HomeScreen(navController, viewmodel, scoreViewModel)
        }
        composable("camerax") {
            ShowCameraPreviewScreen(
                navController,
                cameraViewModel,
                detectedResultListViewModel,
                sharedRouteViewModel
            )
        }
        composable("answer") {
            CameraAnswerScreen(navController, cameraViewModel, sharedRouteViewModel, scoreViewModel)
        }
        composable("pictorial") {
            PictorialBookScreen(navController, categoryName = "과일과 야채", viewmodel, cameraViewModel)
        }
        composable("stage/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            StageScreen(navController, categoryName, viewmodel, scoreViewModel)
            // Set the current category for later use
            CategoryDayManager.setCurrentCategoryName(categoryName)
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
            if (categoryName !in listOf("과일과 야채", "동물", "색")) {
                WordQuizScreen(
                    navController,
                    categoryName,
                    dayIndex,
                    questionIndex,
                    tts,
                    viewmodel,
                    scoreViewModel,
                )

            } else {
                LearnScreen(
                    navController,
                    categoryName,
                    dayIndex,
                    questionIndex,
                    tts,
                    viewmodel,
                    sharedRouteViewModel,
                    scoreViewModel,
                )
            }
        }

        composable("WordInput/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            WordInputScreen(
                navController,
                categoryName,
                dayIndex,
                questionIndex,
                viewmodel,
                scoreViewModel
            )
        }

        composable("camera/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            CameraScreen(
                navController,
                categoryName,
                dayIndex,
                questionIndex,
                viewmodel,
                cameraViewModel,
                sharedRouteViewModel,
            )
        }

        composable("quiz/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            QuizScreen(
                navController,
                categoryName,
                dayIndex,
                questionIndex,
                viewmodel,
                scoreViewModel
            )
        }
        composable("congrats/{categoryName}") { backStackEntry ->
//            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val catgeoryName = CategoryDayManager.getCurrentCategoryName() ?: ""
            CongratScreen(navController, viewmodel, catgeoryName)
        }
        composable("randomPhotoTaker") {
            GPTRandomPhotoTakerScreen(gPTCameraViewModel)
        }

    }
}

@Composable
fun HideSystemBars() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.isSystemBarsVisible = false // 네비게이션 바 & 상태 바 숨기기
        systemUiController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        systemUiController.setSystemBarsColor(Color.Transparent)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SauruspangTheme {
        WordInputScreen(
            navController = rememberNavController(),
            categoryName = "직업",
            dayIndex = 0,
            questionIndex = 0,
            viewModel = viewModel(),
            scoreViewModel = viewModel()
        )
    }
}