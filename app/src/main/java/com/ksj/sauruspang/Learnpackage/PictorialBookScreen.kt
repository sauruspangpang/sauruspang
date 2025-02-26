package com.ksj.sauruspang.Learnpackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R

@Composable
fun PictorialBookScreen(
    navController: NavController,
    categoryName: String,
    viewModel: ProfileViewmodel
) {
    // 현재 프로필이 가진 도감 데이터 (DB에서 Flow로 가져옴)
    val catalogEntries by viewModel.getCatalogEntries().collectAsState(initial = emptyList())
    // 모든 카테고리 목록
    val categories = QuizCategory.allCategories

    Box(modifier = Modifier.fillMaxSize()) {
        // 배경
        Image(
            painter = painterResource(R.drawable.wallpaper_choosecategory),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단 “도감” + 뒤로가기 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_backtochooseda),
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(20.dp))
            }

            // 상단과 그리드 사이에 추가 간격
            Spacer(modifier = Modifier.height(20.dp))

            // 2행 x 3열(또는 동적으로 여러 행) Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxSize()
                    // 전체 그리드 영역에 추가 패딩
                    .padding(horizontal = 20.dp)
                // .padding(vertical = 16.dp) 등으로 세로 여백 추가 가능
                ,
                // 항목들 간 세로 간격
                verticalArrangement = Arrangement.spacedBy(24.dp),
                // 항목들 간 가로 간격
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(categories) { category ->
                    // 1) 이 카테고리 내 모든 QuizQuestion
                    val allQuestions = category.days.flatMap { it.questions }
                    // 2) 중복된 단어(korean)가 있을 경우 제거
                    val uniqueQuestions = allQuestions.distinctBy { it.korean }

                    // 3) 유니크한 단어의 총 개수
                    val totalCount = uniqueQuestions.size

                    // 4) 유니크한 단어 중에서 실제 수집된(도감에 등록된) 개수
                    val collectedCount = uniqueQuestions.count { question ->
                        catalogEntries.any { entry ->
                            entry.answer == question.korean
                        }
                    }

                    // 타일(카테고리) 표시
                    Box(
                        modifier = Modifier
                            // 크기를 좀 더 크게(혹은 작게) 조정 가능
                            .clickable {
                                // 클릭 시 카테고리 상세 화면으로 이동
                                navController.navigate("categoryDetail/${category.name}")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.image_dogam_category),
                            contentDescription = category.name,
                            contentScale = ContentScale.Fit
                        )
                        // 타일 내부 UI
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            // (옵션) 카테고리 썸네일 이미지
                            Image(
                                painter = painterResource(category.thumbnail),
                                contentDescription = category.name,
                                modifier = Modifier.size(60.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // 카테고리 이름
                            Text(
                                text = category.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // 수집 개수 / 전체 개수
                            Text(
                                text = "$collectedCount / $totalCount",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
