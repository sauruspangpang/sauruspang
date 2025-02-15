@file:Suppress("DEPRECATION")

package com.ksj.sauruspang.Learnpackage

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ProfileViewmodel,
    scoreViewModel: ScoreViewModel
) {
    val score by scoreViewModel.correctAnswers
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.choosecategory_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
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
                        .clickable { navController.popBackStack() }
                )

                ProfileBox(scoreViewModel, viewModel)
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.image_photobook),
                    contentDescription = "",
                    modifier = Modifier.clickable { navController.navigate("pictorial") }
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cameravibrate))
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )

                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clickable { navController.navigate("randomPhotoTaker") }
                )

                LazyRow(
                    modifier = Modifier.padding(start = 30.dp, bottom = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    QuizCategory.allCategories.forEachIndexed { index, category ->
                        item(key = index) {
                            CategoryBox(category, navController)
                        }
                    }
                    item { Spacer(modifier = Modifier.width(30.dp)) }
                }
            }
        }
    }
}

@Composable
fun CategoryBox(category: QuizCategory, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = Color(0xFF000000))
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFFFFF))
                .clickable { navController.navigate("stage/${category.name}") }
                .padding(top = 30.dp, start = 30.dp, end = 30.dp)
                .height(150.dp)
                .width(110.dp)
        ) {
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
    val selectedIndex by viewModel.selectedProfileIndex
    val profile = viewModel.profiles
    val selectedProfile = profile.getOrNull(selectedIndex)

    Box(contentAlignment = Alignment.BottomCenter) {
        Image(
            painter = painterResource(R.drawable.image_woodboard),
            contentDescription = "",
        )
        selectedProfile?.let { profile ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 0.dp)
                    .offset(y = (-10).dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(profile.selectedImage),
                    contentDescription = "",
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .padding(end = 15.dp)
                )
                Column(
                    modifier = Modifier.padding(bottom = 0.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
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