package com.ksj.sauruspang.ProfilePackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
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
            painter = painterResource(R.drawable.wallpaper_profile_select),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // 프로필 리스트
        Row(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 20.dp)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(50.dp))
            profiles.forEachIndexed { index, profile ->
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFfefff3))
                        .clickable {
                            viewModel.selectProfile(index)
                            navController.navigate("home")
                        }
                        .width(170.dp)
                        .height(230.dp)
                        .border(3.dp, Color(0xFF00bb2f), shape = RoundedCornerShape(12.dp)),
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
                        text = "생일: ${profile.birth}",
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
                    .width(170.dp)
                    .height(230.dp)
                    .background(Color(0xFFfefff3))
                    .clickable { navController.navigate("main") }
                    .border(3.dp, Color(0xFF00bb2f), shape = RoundedCornerShape(12.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_createprofile),
                        contentDescription = "ADD",
                    )
                }
            }
            Spacer(modifier = Modifier.width(50.dp))
        }
        // 우측 상단 편집 버튼
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
                .border(5.dp, Color(0xFF163a13), shape = RoundedCornerShape(20.dp))
                    .clickable { showEditDialog = true }
        ) {
            Text(
                text = "학습관리",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = Color(0xFF163a13),
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            )
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
