@file:Suppress("DEPRECATION")

package com.ksj.sauruspang.Learnpackage

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
fun HomeScreen(
    navController: NavController,
    viewModel: ProfileViewmodel
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.wallpaper_choosecategory),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .zIndex(1f)
        ) {
            Image(
                painter = painterResource(R.drawable.card_gptcamera),
                contentDescription = "GPT Camera",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            )
            // Lottie Animation
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cameravibrate))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier
                    .clickable { navController.navigate("randomPhotoTaker") }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.image_photobook),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable { navController.navigate("pictorial") }
        )
        Row(){
        Image(
            painter = painterResource(id = R.drawable.icon_changeprofile),
            contentDescription = "button to profile screen",
            modifier = Modifier
//                .align(Alignment.TopStart)
                .clickable {
                    navController.popBackStack()
                }
        )
        ProfileBox(viewModel = viewModel)}
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 30.dp)
        ) {
            Spacer(modifier = Modifier.width(30.dp))
            QuizCategory.allCategories.forEachIndexed { index, category ->
                CategoryBox(category, navController)
                if (index != QuizCategory.allCategories.lastIndex) {
                    Spacer(modifier = Modifier.width(40.dp))
                }
            }
            Spacer(modifier = Modifier.width(30.dp))
        }
    }
}

@Composable
fun CategoryBox(category: QuizCategory, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable { navController.navigate("stage/${category.name}") }
        ) {
            Image(
                painter = painterResource(id = R.drawable.card_category),
                contentDescription = "category background"
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = category.thumbnail),
                    contentDescription = "category thumbnail",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .scale(1.2f)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    category.name,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        if (category.name in listOf("과일과 야채", "색", "동물", "이동수단", "학용품", "옷")) {
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
}

@Composable
fun ProfileBox(viewModel: ProfileViewmodel) {
    val selectedIndex = viewModel.selectedProfileIndex.value
    val profile = viewModel.profiles.getOrNull(selectedIndex)
    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.card_profile_02),
            contentDescription = "",
        )
        profile?.let { profile ->
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(profile.selectedImage),
                    contentDescription = "",
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = profile.name,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.image_star),
                            contentDescription = "",
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "${profile.score * 5}",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
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