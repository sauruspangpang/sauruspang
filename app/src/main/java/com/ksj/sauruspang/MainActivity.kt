package com.ksj.sauruspang

import android.content.Context
import android.net.Uri
import com.ksj.sauruspang.Learnpackage.HomeScreen
import com.ksj.sauruspang.Learnpackage.StageScreen
import com.ksj.sauruspang.Learnpackage.camera.CameraScreen
import com.ksj.sauruspang.Learnpackage.camera.LearnScreen
import com.ksj.sauruspang.Learnpackage.word.WordInputScreen
import com.ksj.sauruspang.Learnpackage.word.WordQuizScreen
import com.ksj.sauruspang.ProfilePackage.MainScreen
import com.ksj.sauruspang.ProfilePackage.ProfilePage
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.ksj.sauruspang.Learnpackage.PictorialBookScreen
import com.ksj.sauruspang.Learnpackage.camera.CameraAnswerScreen
import com.ksj.sauruspang.Learnpackage.camera.CameraViewModel
import com.ksj.sauruspang.Learnpackage.camera.DetectedResultListViewModel
import com.ksj.sauruspang.Learnpackage.camera.SharedRouteViewModel
import com.ksj.sauruspang.Learnpackage.camera.ShowCameraPreviewScreen
import com.ksj.sauruspang.ui.theme.SauruspangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ProfileViewmodel(application)
        setContent {
            SauruspangTheme {
                NaySys(viewModel)
            }
        }
    }
}

@Composable
fun NaySys(viewmodel: ProfileViewmodel) {
    val navController = rememberNavController()
    val cameraViewModel: CameraViewModel = viewModel()
    val sharedRouteViewModel: SharedRouteViewModel = viewModel()
    val detectedResultListViewModel : DetectedResultListViewModel = viewModel()

    NavHost(navController = navController,
        startDestination = if (viewmodel.profiles.isEmpty()) "main" else "profile",
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
            ShowCameraPreviewScreen(navController, cameraViewModel, detectedResultListViewModel)
        }
        composable("answer") {
            CameraAnswerScreen(navController, cameraViewModel, sharedRouteViewModel)
        }
        composable("pictorial") {
            PictorialBookScreen(navController, categoryName = "과일과 야채", viewmodel, cameraViewModel)
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
            if (categoryName !in listOf("과일과 야채", "동물", "색")) {
                WordQuizScreen(navController, categoryName, dayIndex, questionIndex, viewmodel)
            } else {
                LearnScreen(navController, categoryName, dayIndex, questionIndex, viewmodel, sharedRouteViewModel)
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
                cameraViewModel,
                sharedRouteViewModel
            )
        }
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
            viewModel = viewModel()
        )
    }
}