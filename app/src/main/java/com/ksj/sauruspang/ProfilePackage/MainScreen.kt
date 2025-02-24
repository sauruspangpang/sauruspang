package com.ksj.sauruspang.ProfilePackage

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.intel.button.WheelDatePicker2
import com.ksj.sauruspang.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: ProfileViewmodel) {
    var name by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var userProfile by remember { mutableIntStateOf(0) }
    var selectedImage by remember { mutableIntStateOf(R.drawable.test1) }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.createprofile_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
            modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
        )
        Image(
            painter = painterResource(id = R.drawable.image_backhome),
            contentDescription = "button to stagescreen",
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    navController.navigate("profile")
                }
                .align(Alignment.TopStart)
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
                text = "아이 프로필 만들기",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.offset(y = 10.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(selectedImage),
                contentDescription = "background",
                modifier = Modifier
                    .padding(10.dp)
                    .size(180.dp)
                    .border(3.dp, Color(0xFFE8A59C), RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(20.dp))
            Row {
                Column(modifier = Modifier.offset(y = 40.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp) // HIGHLIGHTED
                        ) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                maxLines = 1,
                                placeholder = { Text("이름") },
                                label = {
                                    Text(
                                        "이름",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                    )

                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AccountBox,
                                        contentDescription = "Select date"
                                    )
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Color.White
                                ),
                                modifier = Modifier
                                    .height(75.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .padding(5.dp)
                            )
                        }



                        Spacer(modifier = Modifier.width(60.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFF0022B2),
                                    shape = RoundedCornerShape(12.dp)
                                ) // 배경색 및 둥근 모서리
                                .shadow(
                                    elevation = 8.dp, // 그림자 강도
                                    shape = RoundedCornerShape(12.dp), // 그림자 모양
                                    spotColor = Color(0xFF505050) // 그림자 색상
                                )
                                .clickable {
                                    if (name.length <= 4) {
                                        if (name.isNotEmpty() && birth.isNotEmpty()) {
                                            viewModel.addProfile(
                                                name,
                                                birth,
                                                userProfile++,
                                                selectedImage
                                            )
                                        }
                                        navController.navigate("profile")
                                    }else{
                                        Toast.makeText(context,"이름을 네 자리 이하로 입력해주세요.", Toast.LENGTH_SHORT).show()
                                        name = ""
                                    }
                                }
                        ) {
                            Text(
                                text = "만들기",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFFFF) // 글자색 (흰색)
                                , modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    var showDatePicker by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp) // HIGHLIGHTED
                            .fillMaxWidth() // HIGHLIGHTED
                    ) {
                        OutlinedTextField(
                            value = selectedDate.format(dateFormatter),
                            onValueChange = { },
                            label = {
                                Text(
                                    "생년월일",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select date"
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .height(75.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .padding(5.dp)


                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Transparent)
                                .clickable { showDatePicker = !showDatePicker }
                        )
                    }
                    AnimatedVisibility( // HIGHLIGHTED
                        visible = showDatePicker, // HIGHLIGHTED
                        enter = fadeIn() + expandVertically(), // HIGHLIGHTED
                        exit = fadeOut() + shrinkVertically() // HIGHLIGHTED
                    ) { // HIGHLIGHTED
                        Popup(
                            alignment = Alignment.TopStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 400.dp, height = 330.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .offset(y = -10.dp)
                                    //   .shadow(elevation = 1.dp)
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(16.dp)) // HIGHLIGHTED
                                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                                    .border(2.dp, color = Color.Gray, RoundedCornerShape(16.dp))
                                    .padding(5.dp)

                            ) {
                                WheelDatePicker2(
                                    startDate = selectedDate,
                                    onSnappedDate = { newDate ->
                                        selectedDate = newDate
                                        birth = newDate.format(dateFormatter)
                                    }

                                )
                                Divider(
                                    color = Color.Gray, // You can change the color
                                    thickness = 1.dp, // You can change the thickness
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .offset(y = -45.dp)
                                )
                                Button(
                                    onClick = { showDatePicker = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFFFFFFFF
                                        )
                                    ),
                                    shape = RoundedCornerShape(2.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(top = 10.dp)
                                ) {
                                    Text(
                                        "완료",
                                        color = Color.Black,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                Button(
                                    onClick = { showDatePicker = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFFFFFFFF
                                        )
                                    ),
                                    shape = RoundedCornerShape(2.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(top = 10.dp)
                                ) {
                                    Text(
                                        "취소",
                                        color = Color.Black,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    // }
                    Row {
                        DynamicImageLoding { selectedImage = it }
                    }
                }
            }
        }
    }
}

// 프로필 이미지 선택
@Composable
fun DynamicImageLoding(onImageSelected: (Int) -> Unit) {
    for (i in 1..4) {
        val resourceId = getDrawableResourceId("test$i")
        if (resourceId != 0) {
            Image(
                painter = painterResource(resourceId),
                contentDescription = "foreground",
                modifier = Modifier
                    .padding(10.dp)
                    .border(2.dp, Color(0xFFE8A59C), RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
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

