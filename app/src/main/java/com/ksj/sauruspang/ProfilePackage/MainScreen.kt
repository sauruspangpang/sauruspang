package com.ksj.sauruspang.ProfilePackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.R



@Composable
fun MainScreen(navController: NavController, viewModel: ProfileViewmodel) {
    var name by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf(R.drawable.test1) }
    var role by remember { mutableStateOf("역할") }  // 역할 변수 추가
    var type by remember { mutableStateOf("타입") }  // 타입 변수 추가

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.createprofile_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Text(
            text = "새로운 아이 프로필 만들기",
            fontSize = 32.sp,
            modifier = Modifier.padding(20.dp)
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(selectedImage),
                contentDescription = "background",
                modifier = Modifier
                    .padding(10.dp)
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.padding(10.dp)
                    )
                    Spacer(modifier = Modifier.width(60.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFF0022B2),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (name.isNotEmpty() && birth.isNotEmpty()) {
                                    viewModel.addProfile(
                                        name,
                                        birth,
                                        selectedImage,
                                        role,  // 역할 변수 사용
                                        type,  // 타입 변수 사용
                                        1
                                    )
                                    navController.navigate("profile")
                                }
                            }
                    ) {
                        Text(
                            text = "만들기",
                            fontSize = 28.sp,
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                TextField(
                    value = birth,
                    onValueChange = { birth = it },
                    modifier = Modifier.padding(10.dp)
                )
                Row {
                    DynamicImageLoading { selectedImage = it }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// 프로필 이미지 선택
@Composable
fun DynamicImageLoading(onImageSelected: (Int) -> Unit) {
    for (i in 1..4) {
        val resourceId = getDrawableResourceId("test$i")
        if (resourceId != 0) {
            Image(
                painter = painterResource(resourceId),
                contentDescription = "foreground",
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onImageSelected(resourceId)
                    }
            )
        }
    }
}

// 리소스 ID 가져오기
fun getDrawableResourceId(resourceName: String): Int {
    return try {
        val resourceId = R.drawable::class.java.getField(resourceName).getInt(null)
        resourceId
    } catch (e: Exception) {
        0
    }
}
