package com.ksj.sauruspang.Learnpackage

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.Learnpackage.QuizCategory.Companion.allCategories
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@Composable
fun PictorialBookScreen(
    navController: NavController,
    categoryName: String,
    viewModel: ProfileViewmodel
) {
    // 기존 디자인의 탭 및 스케치북 레이아웃 유지
    val categories = allCategories.map { it.name }
    val scrollState = rememberScrollState()
    var selectedCategory by remember { mutableStateOf(categoryName) }
    val selectedIndex = categories.indexOf(selectedCategory).coerceAtLeast(0)

    // 프로필별 도감 데이터는 ProfileViewmodel의 getCatalogEntries()를 통해 불러옴
    // (이 Flow는 현재 선택된 프로필의 도감 항목을 반환합니다.)
    val catalogEntries by viewModel.getCatalogEntries().collectAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.choosecategory_wallpaper),
            contentDescription = "배경 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
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
                            navController.popBackStack()
                        }
                )
                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
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
                    .height(400.dp)
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.Start
            ) {
                catalogEntries.forEach { entry ->
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(horizontal = 10.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.image_sketchbook),
                            contentDescription = "스케치북 배경",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .width(250.dp)
                                .wrapContentHeight()
                        )
                        Column(
                            modifier = Modifier
                                .height(200.dp)
                                .width(200.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Convert ByteArray to Bitmap
                            val bitmap = BitmapFactory.decodeByteArray(entry.image, 0, entry.image.size)
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Captured Image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxSize()
                            )
                            Text(
                                text = entry.answer,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
