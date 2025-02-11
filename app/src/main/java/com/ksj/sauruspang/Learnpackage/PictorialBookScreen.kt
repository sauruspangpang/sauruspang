package com.ksj.sauruspang.Learnpackage

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.Learnpackage.QuizCategory.Companion.allCategories
import com.ksj.sauruspang.Learnpackage.camera.CameraViewModel
import com.ksj.sauruspang.Learnpackage.camera.CapturedImage
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@Composable
fun PictorialBookScreen(
    navController: NavController,
    categoryName: String,
    viewModel: ProfileViewmodel,
    cameraViewModel: CameraViewModel
) {
    val categories = allCategories.map { it.name }
    val scrollState = rememberScrollState()
    var selectedCategory by remember {
        mutableStateOf(categories.find { it == categoryName } ?: "과일과 야채")
    }
    var selectedIndex = categories.indexOf(selectedCategory).coerceAtLeast(0)
    var indexValue = cameraViewModel.correctWordList.size
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.choosecategory_wallpaper),
            contentDescription = "배경 이미지",
            contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
            modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_backhome),
                    contentDescription = "카테고리 선택으로 이동",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("home")
                        }
                )
                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
//                    modifier = Modifier.width(IntrinsicSize.Min), // 내용에 맞게 너비 조절
                    edgePadding = 0.dp,
                    modifier = Modifier.wrapContentWidth(),
                    containerColor = Color.Transparent,
                    contentColor = Color.Black

                ) {
                    categories.forEachIndexed { index, category ->
                        Tab(
                            selected = selectedIndex == index,
                            onClick = { selectedCategory = category }
                        ) {
                            Text(category, modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                for (index in 0..indexValue - 1) {
                    Column(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                Color.Black,
                                RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            bitmap = cameraViewModel.correctImageList[index].asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Text(
                            cameraViewModel.correctWordList[index],
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}