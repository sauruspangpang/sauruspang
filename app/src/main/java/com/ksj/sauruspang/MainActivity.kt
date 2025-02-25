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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
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

        // AdMob 초기화
        MobileAds.initialize(this) {}

        // 앱 콘텐츠가 시스템 창(상태바, 네비게이션바) 뒤에 그려지도록 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            }
        }
        val viewModel = ProfileViewmodel(application)
        setContent {
            val scoreViewModel: ScoreViewModel = viewModel()
            // RequestPermissions(), HideSystemBars()는 프로젝트에서 별도로 구현된 함수라고 가정
            RequestPermissions()
            HideSystemBars()
            SauruspangTheme {
                NaySys(viewModel, tts, scoreViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }
}

@Composable
fun NaySys(viewmodel: ProfileViewmodel, tts: TextToSpeech, scoreViewModel: ScoreViewModel) {
    val navController = rememberNavController()
    val cameraViewModel: CameraViewModel = viewModel()
    val sharedRouteViewModel: SharedRouteViewModel = viewModel()
    val detectedResultListViewModel: DetectedResultListViewModel = viewModel()
    val gPTCameraViewModel: GPTCameraViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = if (viewmodel.profiles.isEmpty()) "main" else "profile",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable("main") { MainScreen(navController, viewmodel) }
        composable("profile",
            enterTransition = {
                slideInHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                    fullWidth / -1
                }
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                    fullWidth / 1
                }
            }
        ) { ProfilePage(navController, viewmodel) }
        composable("home",
            enterTransition = {
                slideInHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                    fullWidth / -1
                }
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(durationMillis = 500)) { fullWidth ->
                    fullWidth / 1
                }
            }
        ) { HomeScreen(navController, viewmodel, scoreViewModel) }
        composable("camerax") {
            ShowCameraPreviewScreen(navController, cameraViewModel, detectedResultListViewModel, sharedRouteViewModel)
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
            CategoryDayManager.setCurrentCategoryName(categoryName)
        }
        composable("learn/{categoryName}/{dayIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            navController.navigate("learn/$categoryName/$dayIndex/0")
        }
        composable("learn/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            if (categoryName !in listOf("과일과 야채", "동물", "색")) {
                WordQuizScreen(navController, categoryName, dayIndex, questionIndex, tts, viewmodel, scoreViewModel)
            } else {
                LearnScreen(navController, categoryName, dayIndex, questionIndex, tts, viewmodel, sharedRouteViewModel, scoreViewModel)
            }
        }
        composable("WordInput/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            WordInputScreen(navController, categoryName, dayIndex, questionIndex, tts, viewmodel, scoreViewModel)
        }
        composable("camera/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            CameraScreen(navController, categoryName, dayIndex, questionIndex, viewmodel, cameraViewModel, sharedRouteViewModel)
        }
        composable("quiz/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            QuizScreen(navController, categoryName, dayIndex, questionIndex, viewmodel, scoreViewModel)
        }
        composable("congrats/{categoryName}") { backStackEntry ->
            val catgeoryName = CategoryDayManager.getCurrentCategoryName() ?: ""
            CongratScreen(navController, viewmodel, catgeoryName)
        }
        composable("randomPhotoTaker") {
            GPTRandomPhotoTakerScreen(gPTCameraViewModel, tts, scoreViewModel, navController)
        }
    }
}

@Composable
fun HideSystemBars() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.isSystemBarsVisible = false
        systemUiController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        systemUiController.setSystemBarsColor(Color.Transparent)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SauruspangTheme {
        WordQuizScreen(
            navController = rememberNavController(),
            categoryName = "직업",
            dayIndex = 0,
            questionIndex = 0,
            viewModel = viewModel(),
            scoreViewModel = viewModel(),
            tts = TextToSpeech(null, null)
        )
    }
}
