package com.ksj.sauruspang.Learnpackage

import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
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
import com.ksj.sauruspang.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: ProfileViewmodel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.choosecategory_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
            modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row() {
                Button(onClick = {
                    val categoryName = "과일과 야채"
                    val dayIndex = 0
                    val questionIndex = 2
                    navController.navigate("camera/$categoryName/$dayIndex/$questionIndex")
                }) {
                    Text("shortcut")
                }

                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .size(60.dp)
                        .padding(5.dp)
                        .clickable { navController.navigate("randomPhotoTaker") }
                ) {
                    Text("temp camera")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_backhome),
                    contentDescription = "button to stagescreen",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("profile")
                        }
                )
                Box(contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .offset(y = (-30).dp)) {
                    Image(
                        painter = painterResource(R.drawable.image_woodboard),
                        contentDescription = "",

                    )
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 30.dp), // 좌우 여백 추가
                        verticalAlignment = Alignment.CenterVertically // 내부 요소도 중앙 정렬
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.ellipse_1),
                            contentDescription = "",
                            Modifier.scale(0.8f)
                        )
                        Column(
                            modifier = Modifier
                                .padding(bottom = 20.dp), // 내부 요소 패딩
                            verticalArrangement = Arrangement.SpaceBetween, // 내부 요소 정렬
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                "Hello 박민준",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically // 이미지와 텍스트를 같은 높이로 정렬
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.image_starpoint),
                                    contentDescription = "",
                                    modifier = Modifier.size(36.dp) // 원하는 크기로 조정
                                )
                                Spacer(modifier = Modifier.width(10.dp)) // 이미지와 숫자 사이 간격
                                Text(
                                    "245",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.image_photobook),
                    contentDescription = "",
                    modifier = Modifier
                        .size(70.dp)
                        .clickable { navController.navigate("pictorial") }

                )

            }
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 30.dp, bottom = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp) // 각 Box 사이의 간격 추가
            ) {
                QuizCategory.allCategories.forEach { category ->
                    CategoryBox(category, navController)
                }

            }

        }
    }

}


@Composable
fun CategoryBox(category: QuizCategory, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 박스 본체
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    spotColor = Color(0xFF000000) // 그림자 색상
                )
                .clip(RoundedCornerShape(12.dp)) // 둥근 모서리 적용
                .background(Color(0xFFFFFFFF)) // 배경색 적용
                .clickable { navController.navigate("stage/${category.name}") }
                .padding(top = 30.dp, start = 30.dp, end = 30.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // 가운데 정렬
                modifier = Modifier
                    .fillMaxWidth() // 전체 크기 채우기
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = category.thumbnail),
                    contentDescription = "category thumbnail",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(20.dp)) // 20dp 간격 추가
                Text(
                    category.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(20.dp)) // 20dp 간격 추가
            }
        }

        Image(
            painter = painterResource(id = R.drawable.image_cameramark),
            contentDescription = "CameraMark",
            modifier = Modifier
                .offset(x = 10.dp, y = (-20).dp) // 오른쪽 위로 이동
                .width(80.dp)
                .align(Alignment.TopEnd) // 상위 박스의 오른쪽 상단에 배치
        )
    }
}



