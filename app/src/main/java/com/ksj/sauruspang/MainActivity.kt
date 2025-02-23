package com.ksj.sauruspang

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ksj.sauruspang.Learnpackage.CategoryDayManager
import com.ksj.sauruspang.Learnpackage.CategoryDetailScreen
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
import com.ksj.sauruspang.Learnpackage.camera.LearnScreen
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

        // 앱 콘텐츠가 시스템 창(상태바, 네비게이션바) 뒤에 그려지도록 설정
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 상태바와 네비게이션바 숨기기 (전체 시스템 바 숨김)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())

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
fun NaySys(
    viewmodel: ProfileViewmodel,
    tts: TextToSpeech,
    scoreViewModel: ScoreViewModel
) {
    val navController = rememberNavController()

    // 화면 전환 시 사용할 뷰모델들
    val cameraViewModel: CameraViewModel = viewModel()
    val sharedRouteViewModel: SharedRouteViewModel = viewModel()
    val detectedResultListViewModel: DetectedResultListViewModel = viewModel()
    val gPTCameraViewModel: GPTCameraViewModel = viewModel()

    // 전환 애니메이션 시간을 500ms → 300ms 로 단축
    // (필요시 EnterTransition.None / ExitTransition.None 으로 완전히 제거 가능)
    val transitionTime = 300

    NavHost(
        navController = navController,
        startDestination = if (viewmodel.profiles.isEmpty()) "main" else "profile",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable("main") {
            MainScreen(navController, viewmodel)
        }
        composable(
            "profile",
            enterTransition = {
                slideInHorizontally(animationSpec = tween(transitionTime)) { fullWidth ->
                    // 왼쪽에서 오른쪽으로 밀려 들어옴
                    fullWidth / -1
                }
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(transitionTime)) { fullWidth ->
                    // 오른쪽으로 밀려 나감
                    fullWidth / 1
                }
            }
        ) {
            ProfilePage(navController, viewmodel)
        }
        composable(
            "home",
            enterTransition = {
                slideInHorizontally(animationSpec = tween(transitionTime)) { fullWidth ->
                    fullWidth / -1
                }
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(transitionTime)) { fullWidth ->
                    fullWidth / 1
                }
            }
        ) {
            HomeScreen(navController, viewmodel)
        }

        composable("camerax") {
            ShowCameraPreviewScreen(navController, cameraViewModel, detectedResultListViewModel, sharedRouteViewModel)
        }
        composable("answer") {
            CameraAnswerScreen(navController, cameraViewModel, sharedRouteViewModel, scoreViewModel, viewmodel)
        }
        composable("pictorial") {
            // 도감(메인) 화면
            PictorialBookScreen(navController, categoryName = "과일과 야채", viewModel = viewmodel)
        }
        // 도감 상세 화면
        composable(
            route = "categoryDetail/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            CategoryDetailScreen(navController, backStackEntry, viewmodel)
        }

        // StageScreen
        composable("stage/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            StageScreen(navController, categoryName, viewmodel)
            // 현재 카테고리 설정
            CategoryDayManager.setCurrentCategoryName(categoryName)
        }

        // LearnScreen 진입점
        composable("learn/{categoryName}/{dayIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            // 첫 문제(0번)으로 자동 이동
            navController.navigate("learn/$categoryName/$dayIndex/0")
        }

        // LearnScreen 실제 퀴즈
        composable("learn/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0

            // 과일, 동물, 색 카테고리는 LearnScreen 사용
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

        // WordInputScreen
        composable("WordInput/{categoryName}/{dayIndex}/{questionIndex}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val dayIndex = backStackEntry.arguments?.getString("dayIndex")?.toInt() ?: 0
            val questionIndex = backStackEntry.arguments?.getString("questionIndex")?.toInt() ?: 0
            WordInputScreen(
                navController,
                categoryName,
                dayIndex,
                questionIndex,
                tts,
                viewmodel,
                scoreViewModel
            )
        }

        // 카메라 촬영(정답 등록) 화면
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

        // QuizScreen
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

        // 최종 완료 화면
        composable("congrats/{categoryName}") { backStackEntry ->
            val catgeoryName = CategoryDayManager.getCurrentCategoryName() ?: ""
            CongratScreen(navController, viewmodel, catgeoryName)
        }

        // GPT Random Photo Taker
        composable("randomPhotoTaker") {
            GPTRandomPhotoTakerScreen(gPTCameraViewModel, tts, scoreViewModel, navController)
        }
    }
}

@Composable
fun HideSystemBars() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        // 완전히 숨기기
        systemUiController.isSystemBarsVisible = false
        systemUiController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // 투명색으로 설정
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
