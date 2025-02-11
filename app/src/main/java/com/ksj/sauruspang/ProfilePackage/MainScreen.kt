package com.ksj.sauruspang.ProfilePackage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.navigation.NavController
import androidx.lifecycle.lifecycleScope
import androidx.room.Dao
import androidx.room.Entity
import com.ksj.sauruspang.ProfilePackage.Room.User
import com.ksj.sauruspang.ProfilePackage.Room.UserDao
import com.ksj.sauruspang.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


@Composable
fun MainScreen(navController: NavController, viewModel: ProfileViewmodel, userViewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var userProfile by remember { mutableIntStateOf(0) }
    var selectedImage by remember { mutableIntStateOf(R.drawable.test1) }

    fun saveUserUid(context: Context, uid: Int) {
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("user_uid", uid)  // `user_uid`라는 키로 `uid` 저장
        editor.apply()  // 비동기적으로 저장
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.createprofile_wallpaper),
            contentDescription = null,
            contentScale = ContentScale.Crop,  // 화면에 맞게 꽉 채우기
            modifier = Modifier.matchParentSize()  // Box의 크기와 동일하게 설정
        )
        Text(
            text = "새로운 아이 프로필 만들기",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .padding(20.dp)
        )
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
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(20.dp))
            Row {
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
                                ) // 배경색 및 둥근 모서리
                                .shadow(
                                    elevation = 8.dp, // 그림자 강도
                                    shape = RoundedCornerShape(12.dp), // 그림자 모양
                                    spotColor = Color(0xFF505050) // 그림자 색상
                                )
                                .clickable {
                                    userViewModel.saveUser(
                                        name,
                                        birth,
                                        selectedImage,
                                        clearedImages = listOf(),
                                        clearedWords = listOf()
                                    )
                                    if (name.isNotEmpty() && birth.isNotEmpty()) {
                                        viewModel.addProfile(
                                            name,
                                            birth,
                                            userProfile++,
                                            selectedImage,
                                            listOf(),
                                            listOf()
                                        )
                                    }
                                    navController.navigate("profile")
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
                    TextField(
                        value = birth,
                        onValueChange = { birth = it },
                        modifier = Modifier.padding(10.dp)
                    )
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

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun base64ToBitmap(encodedString: String): Bitmap {
    val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

