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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.R
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel

@Composable
fun ProfilePage(navController: NavController, viewModel: ProfileViewmodel) {
    val profiles = viewModel.profiles
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // 편집 다이얼로그 노출 여부
    var showEditDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.kidsprofile_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
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
        // 우측 상단 편집 버튼
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = "편집",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .background(Color(0xFF0022B2), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable { showEditDialog = true }
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
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFAE64))
                        .drawBehind {
                            drawRoundRect(
                                color = Color(0xFFFDD4AA),
                                topLeft = Offset(-8.dp.toPx(), -8.dp.toPx()),
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
                    Text(
                        text = "Score: ${profile.score}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
                Spacer(modifier = Modifier.padding(30.dp))
            }
            // 새 프로필 추가 버튼
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .width(150.dp)
                    .height(200.dp)
                    .background(Color(0xFFFFAE64))
                    .drawBehind {
                        drawRoundRect(
                            color = Color(0xFFFDD4AA),
                            topLeft = Offset(-8.dp.toPx(), -8.dp.toPx()),
                            size = size,
                            cornerRadius = CornerRadius(12.dp.toPx())
                        )
                    }
                    .clickable { navController.navigate("main") },
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "ADD",
                        modifier = Modifier.size(80.dp),
                        tint = Color.Black
                    )
                }
                Text(
                    text = "새로운 프로필 만들기",
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(vertical = 20.dp)
                )
            }
        }
    }

// 편집 다이얼로그 (삭제 기능 포함)
    if (showEditDialog) {
        EditProfileDialog(
            profiles = profiles,
            onDismiss = { showEditDialog = false },
            onDelete = { selectedProfile ->
                viewModel.deleteProfile(selectedProfile)
                showEditDialog = false
            }
        )
    }
}
