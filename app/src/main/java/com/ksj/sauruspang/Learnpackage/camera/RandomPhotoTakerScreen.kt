package com.ksj.sauruspang.Learnpackage.camera

import android.app.Activity
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

import com.ksj.sauruspang.R
import com.ksj.sauruspang.util.CameraUsageManager

@Composable
fun GPTRandomPhotoTakerScreen(
    camViewModel: GPTCameraViewModel,
    tts: TextToSpeech?,

    navController: NavController
) {
    val context = LocalContext.current
    val activity = context as? Activity

    // 1) 카메라 사용 횟수
    val usageManager = remember { CameraUsageManager(context) }
    var usageCount by remember { mutableStateOf(usageManager.getUsageCount()) }

    // 2) 촬영된 이미지 및 결과 화면
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    var showResultScreen by remember { mutableStateOf(false) }

    // 3) 리워드 광고
    var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }
    var adLoadingError by remember { mutableStateOf("") }

    // 광고 로드
    LaunchedEffect(Unit) {
        loadRewardAd(context) { ad, error ->
            rewardedAd = ad
            adLoadingError = error ?: ""
        }
    }

    fun showRewardAd() {
        val ad = rewardedAd
        if (ad != null && activity != null) {
            ad.show(activity) { _: RewardItem ->
                // 광고 시청 → 4회 충전
                usageManager.addUsage(4)
                usageCount = usageManager.getUsageCount()
                // 광고 본 뒤 재로드
                loadRewardAd(context) { newAd, error ->
                    rewardedAd = newAd
                    adLoadingError = error ?: ""
                }
            }
        }
    }

    // ✅ Scaffold 대신 Box로 전체 화면 구성
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDD4AA))
    ) {
        // 상단 좌측 홈 아이콘 (TopStart)
        Image(
            painter = painterResource(id = R.drawable.icon_backtochooseda),
            contentDescription = "홈으로",
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .align(Alignment.TopStart)
                .clickable {
                    navController.popBackStack("home", false)
                }
        )

        // 본문 로직
        when {
            // 결과 화면
            showResultScreen && capturedImage != null -> {
                GPTCameraResultScreen(
                    capturedImage = capturedImage!!,
                    prediction = camViewModel.predictionResult.value,
                    onRetake = {
                        showResultScreen = false
                        camViewModel.predictionResult.value = ""
                    },
                    tts = tts,
                    navController = navController,
                    remainingUsage = usageCount
                )
            }

            // 촬영 가능 횟수가 남아있으면 촬영
            usageCount > 0 -> {
                GPTCameraCaptureScreen(
                    remainingUsage = usageCount,
                    onImageCaptured = { bitmap ->
                        usageManager.decrementUsage()
                        usageCount = usageManager.getUsageCount()
                        capturedImage = bitmap
                        camViewModel.analyzeImage(bitmap)
                        showResultScreen = true
                    }
                )
            }

            // 횟수 0 → 광고 유도
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

// 리워드 광고 로드 함수
private fun loadRewardAd(
    context: android.content.Context,
    onResult: (RewardedAd?, String?) -> Unit
) {
    val adRequest = AdRequest.Builder().build()
    RewardedAd.load(
        context,
        "ca-app-pub-3940256099942544/5224354917", // 테스트용; 출시 시 교체
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
