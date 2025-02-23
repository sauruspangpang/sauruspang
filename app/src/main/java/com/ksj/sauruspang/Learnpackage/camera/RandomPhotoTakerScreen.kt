package com.ksj.sauruspang.Learnpackage.camera

import android.app.Activity
import android.content.Context  // 반드시 추가
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.ksj.sauruspang.Learnpackage.ScoreViewModel
import com.ksj.sauruspang.util.CameraUsageManager

/**
 * GPT 카메라 사용 횟수(최대 4회) + 리워드 광고로 충전
 */
@Composable
fun GPTRandomPhotoTakerScreen(
    camViewModel: GPTCameraViewModel,
    tts: TextToSpeech?,
    scoreViewModel: ScoreViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val activity = context as? Activity

    // 1) 카메라 사용 횟수 매니저
    val usageManager = remember { CameraUsageManager(context) }
    var usageCount by remember { mutableStateOf(usageManager.getUsageCount()) }

    // 2) 카메라 촬영/결과 화면 제어
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    var showResultScreen by remember { mutableStateOf(false) }

    // 3) 리워드 광고
    var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }
    var adLoadingError by remember { mutableStateOf("") }

    // 화면 진입 시 광고 로드
    LaunchedEffect(Unit) {
        loadRewardAd(context) { ad, error ->
            rewardedAd = ad
            adLoadingError = error ?: ""
        }
    }

    /**
     * 리워드 광고 보여주기 함수
     */
    fun showRewardAd() {
        val ad = rewardedAd
        if (ad != null && activity != null) {
            ad.show(activity) { _: RewardItem ->
                // 광고 성공 시 1회 충전
                usageManager.addUsage(1)
                usageCount = usageManager.getUsageCount()
                // 광고 후 재로드
                loadRewardAd(context) { newAd, error ->
                    rewardedAd = newAd
                    adLoadingError = error ?: ""
                }
            }
        }
    }

    /**
     * UI
     */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDD4AA))
    ) {
        when {
            // 1) 결과 화면 보여주기
            showResultScreen && capturedImage != null -> {
                GPTCameraResultScreen(
                    capturedImage = capturedImage!!,
                    prediction = camViewModel.predictionResult.value,
                    onRetake = {
                        // 다시 촬영
                        showResultScreen = false
                        camViewModel.predictionResult.value = ""
                    },
                    tts = tts,
                    scoreViewModel = scoreViewModel,
                    navController = navController
                )
            }
            // 2) 사용 횟수가 남았으면 카메라 촬영
            usageCount > 0 -> {
                GPTCameraCaptureScreen { bitmap ->
                    // 촬영 성공 시 1회 소모
                    usageManager.decrementUsage()
                    usageCount = usageManager.getUsageCount()

                    // GPT 분석
                    capturedImage = bitmap
                    camViewModel.analyzeImage(bitmap)
                    showResultScreen = true
                }
            }
            // 3) 남은 횟수가 0이면 광고 유도
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("남은 촬영 횟수가 없습니다.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showRewardAd() }) {
                        Text("광고 시청하여 횟수 충전")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (adLoadingError.isNotEmpty()) {
                        Text("광고 로드 에러: $adLoadingError")
                    }
                }
            }
        }
    }
}

/**
 * 리워드 광고 로드 함수
 * - 테스트용: "ca-app-pub-3940256099942544/5224354917"
 * - 실제 출시 시 위 ID를 본인 것으로 교체
 */
private fun loadRewardAd(
    context: Context,
    onResult: (RewardedAd?, String?) -> Unit
) {
    val adRequest = AdRequest.Builder().build()
    RewardedAd.load(
        context,
        "ca-app-pub-3940256099942544/5224354917", // 테스트용 리워드 광고 단위; 실제 출시 시 교체
        adRequest,
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                onResult(null, adError.message)
            }
            override fun onAdLoaded(ad: RewardedAd) {
                onResult(ad, null)
            }
        }
    )
}
