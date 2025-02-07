package com.ksj.sauruspang

import Learnpackage.camera.CameraScreen
import Learnpackage.HomeScreen
import Learnpackage.camera.LearnScreen
import Learnpackage.camera.QuizScreen
import Learnpackage.StageScreen
import Learnpackage.camera.CameraAnswerScreen
import Learnpackage.camera.CameraViewModel
import Learnpackage.camera.ShowCameraPreviewScreen
import Learnpackage.word.WordInputScreen
import Learnpackage.word.WordQuizScreen
import ProfilePackage.MainScreen
import ProfilePackage.ProfilePage
import ProfilePackage.ProfileViewmodel
import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ksj.sauruspang.ui.theme.SauruspangTheme

class MainActivity : ComponentActivity() {

    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Implement camera related  code
            } else {
                // Camera permission denied (Handle denied operation)
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) -> {
                // Camera permission already granted
                // Implement camera related code
            }

            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }

        setContent {
            SauruspangTheme {
                NavSys(viewmodel = ProfileViewmodel())
            }
        }

    }
}

@Composable
fun NavSys(viewmodel: ProfileViewmodel) {
    val navController = rememberNavController()
    val cameraViewModel: CameraViewModel = viewModel()
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
        composable("camerax") {
            ShowCameraPreviewScreen(navController, cameraViewModel)
        }
        composable("answer") {
            CameraAnswerScreen(navController, cameraViewModel)
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
                LearnScreen(navController, categoryName, dayIndex, questionIndex, viewmodel)
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
            CameraScreen(
                navController,
                categoryName,
                dayIndex,
                questionIndex,
                viewmodel,
                cameraViewModel
            )
        }
    }
}

@Preview(widthDp = 1000, heightDp = 450, showBackground = true)
@Composable
fun GreetingPreview() {
    SauruspangTheme {
        QuizScreen(
            navController = rememberNavController(),
            categoryName = "과일",
            0,
            0,
            ProfileViewmodel()
        )
    }
}
