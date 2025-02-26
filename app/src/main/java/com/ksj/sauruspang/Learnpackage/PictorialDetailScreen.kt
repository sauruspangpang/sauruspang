package com.ksj.sauruspang.Learnpackage

import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@Composable
fun PictorialDetailScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    viewModel: ProfileViewmodel
) {
    // NavHost에서 "categoryDetail/{categoryName}" 형태로 전달받은 카테고리 이름
    val categoryName = backStackEntry.arguments?.getString("categoryName") ?: return
    // QuizCategory.allCategories에서 해당 카테고리 찾기
    val currentCategory = QuizCategory.allCategories.find { it.name == categoryName } ?: return

    // 현재 프로필이 가진 도감 정보
    val catalogEntries by viewModel.getCatalogEntries().collectAsState(emptyList())

    // 1) 이 카테고리의 모든 QuizQuestion 모으기
    val allQuestions = currentCategory.days.flatMap { it.questions }
    // 2) 중복 제거 (동일 korean 값을 갖는 문제는 1회만 표시)
    val uniqueQuestions = allQuestions.distinctBy { it.korean }

    // 그레이스케일 효과용 ColorMatrix: 채도 0으로 설정하여 흑백 이미지로 변환
    val grayscaleMatrix = ColorMatrix().apply { setToSaturation(0f) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 배경
        Image(
            painter = painterResource(R.drawable.wallpaper_dogaminside),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // 상단 바 (뒤로가기 + 제목)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_backtochooseda),
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                )
                Text(
                    text = currentCategory.name,
                    fontSize = 24.sp,
                    color = Color(0xFF00BB2F), // 제목색 00bb2f (알파값 FF 추가)
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .background(
                            color = Color(0xFFFFFede), // 배경색 fffede (알파값 FF 추가)
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            BorderStroke(3.dp, Color(0xFF00BB2F)), // 테두리색 00bb2f (알파값 FF 추가)
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp) // 내부 여백
                )
            }

            // 그리드 레이아웃으로 모든 항목 표시
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(uniqueQuestions) { question ->
                    // 도감에 저장되었는지 확인 (entry.answer가 question.korean과 동일하면 등록됨)
                    val matchedEntry = catalogEntries.find { entry ->
                        entry.answer == question.korean
                    }

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(painter = painterResource(id = R.drawable.image_dogam_item),
                            contentDescription = null,)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (matchedEntry != null) {
                                // 등록된 항목이면 저장된 이미지 표시
                                val bitmap = BitmapFactory.decodeByteArray(
                                    matchedEntry.image, 0, matchedEntry.image.size
                                )
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = question.korean,
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                // 미등록이면 QuizQuestion의 imageId를 활용하여
                                // 베이지색 박스 배경 위에 그레이스케일 이미지 표시
                                Box(
                                    modifier = Modifier, // 베이지색 배경
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column {
                                        Image(
                                            painter = painterResource(id = question.imageId),
                                            contentDescription = question.korean,
                                            modifier = Modifier.size(60.dp),
                                            contentScale = ContentScale.Fit,
                                            colorFilter = ColorFilter.colorMatrix(grayscaleMatrix)
                                        )
                                        Text(
                                            text = question.korean,
                                            textAlign = TextAlign.Center,
                                            fontSize = 14.sp,
                                            modifier = Modifier.widthIn(min = 60.dp)
                                        )
                                    }

                                }
                            }
                            // 항목 이름(한국어) 표시
                        }
                    }
                }
            }
        }
    }
}
