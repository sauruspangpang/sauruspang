package com.ksj.sauruspang.ProfilePackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.R


@Composable
fun ProfilePage(navController: NavController, viewModel: ProfileViewmodel) {
    val profiles = viewModel.profiles
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.kidsprofile_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
            modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 15.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.image_banner),
                contentDescription = "banner",
            )
            Text(
                text = "아이 프로필 선택하기",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.offset(y = 10.dp)
            )
        }

        // 프로필 리스트
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp)
                .offset(y = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            profiles.forEachIndexed { index, profile ->
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp)) // 둥근 모서리
                        .background(Color(0xFFFFAE64)) // 배경색
                        .drawBehind {
                            drawRoundRect(
                                color = Color(0xFFFDD4AA), // 그림자 색상
                                topLeft = Offset(-8.dp.toPx(), -8.dp.toPx()), // 우측 하단으로 이동
                                size = size,
                                cornerRadius = CornerRadius(12.dp.toPx())
                            )
                        }
                        .clickable {
                            viewModel.selectProfile(index)
                            navController.navigate("home")
                        },
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = profile.selectedImage),
                        contentDescription = "profile image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .padding(20.dp)
                            .size(120.dp)
                    )

                    Text(
                        text = "이름: ${profile.name}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "생년월일: ${profile.birth}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
                Spacer(modifier = Modifier.padding(30.dp))
            }

            // 새 프로필 추가 버튼
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp)) // 둥근 모서리
                    .width(150.dp)
                    .height(200.dp)
                    .background(Color(0xFFFFAE64)) // 배경색
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFFDD4AA), // 그림자 색상
                            topLeft = Offset(-8.dp.toPx(), -8.dp.toPx()), // 우측 하단으로 이동
                            size = size,
                            cornerRadius = CornerRadius(12.dp.toPx())
                        )
                    }
                    .clickable { navController.navigate("main") } // 새 프로필 추가 화면으로 이동
                ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp)) // 둥근 모서리 적용
                        .background(Color.White), // 흰색 배경 추가
                    contentAlignment = Alignment.Center // 아이콘을 중앙 정렬
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "ADD",
                        modifier = Modifier.size(80.dp), // 아이콘 크기 조정 (배경보다 작게)
                        tint = Color.Black // 아이콘 색상 설정 (원하는 색상으로 변경 가능)
                    )
                }
            }
        }
    }
}