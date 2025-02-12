@file:Suppress("DEPRECATION")

package com.ksj.sauruspang.Learnpackage

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: ProfileViewmodel, scoreViewModel: ScoreViewModel) {
    val score by scoreViewModel.correctAnswers
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.choosecategory_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
            modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_backhome),
                    contentDescription = "button to profile screen",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                ProfileBox(scoreViewModel, viewModel)
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.image_photobook),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable { navController.navigate("pictorial") }

                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Lottie Animation
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cameravibrate))
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )

                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier.padding(horizontal = 20.dp)
                    .clickable { navController.navigate("randomPhotoTaker") }
                )

                // Scrollable Row using LazyRow
                LazyRow(
                    modifier = Modifier
                        .padding(start = 30.dp, bottom = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    // 반복적으로 카테고리 박스 생성
                    QuizCategory.allCategories.forEachIndexed { index, category ->
                        item(key = index) {
                            CategoryBox(category, navController)
                        }
                    }
                    // 마지막 Spacer
                    item {
                        Spacer(modifier = Modifier.width(30.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryBox(category: QuizCategory, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 박스 본체
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    spotColor = Color(0xFF000000) // 그림자 색상
                )
                .clip(RoundedCornerShape(12.dp)) // 둥근 모서리 적용
                .background(Color(0xFFFFFFFF)) // 배경색 적용
                .clickable { navController.navigate("stage/${category.name}") }
                .padding(top = 30.dp, start = 30.dp, end = 30.dp)
                .height(150.dp)
                .width(110.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // 가운데 정렬
                modifier = Modifier
                    .fillMaxWidth() // 전체 크기 채우기
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = category.thumbnail),
                    contentDescription = "category thumbnail",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .scale(1.2f)
                )
                Spacer(modifier = Modifier.height(20.dp)) // 20dp 간격 추가
                Text(
                    category.name,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(20.dp)) // 20dp 간격 추가
            }
        }

        Image(
            painter = painterResource(id = R.drawable.image_cameramark),
            contentDescription = "CameraMark",
            modifier = Modifier
                .clickable {
                    Log.d("Navigation", "Navigating to gpt_camera_preview")
                    navController.navigate("gpt_camera_preview")
                }
                .offset(x = 10.dp, y = (-20).dp)
                .width(80.dp)
                .align(Alignment.TopEnd)
        )


    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ProfileBox(scoreViewModel: ScoreViewModel, viewModel: ProfileViewmodel) {
    val selectedIndex by viewModel.selectedProfileIndex // 선택된 인덱스 가져오기
    val profile = viewModel.profiles // 프로필 리스트 가져오기
    val selectedProfile = profile.getOrNull(selectedIndex)

    Box(contentAlignment = Alignment.BottomCenter) {
        Image(
            painter = painterResource(R.drawable.image_woodboard),
            contentDescription = "",
        )

        selectedProfile?.let { profile ->
            Row(
                modifier = Modifier.padding(horizontal = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(profile.selectedImage),
                    contentDescription = "",
                    Modifier.scale(0.6f).clip(CircleShape)
                )
                Column(
                    modifier = Modifier.padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = profile.name,
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.image_starpoint),
                            contentDescription = "",
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        val score by scoreViewModel.correctAnswers
                        Text(
                            "${score * 5}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } ?: run {
            Text(
                text = "No Profile Selected",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}