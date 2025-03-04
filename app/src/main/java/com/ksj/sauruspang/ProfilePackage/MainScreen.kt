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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.wallpaper_profile_create),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        )
        Image(
            painter = painterResource(id = R.drawable.icon_backtochooseda),
            contentDescription = "button to stagescreen",
            modifier = Modifier
                .padding(top = 10.dp, start = 10.dp)
                .clickable {
                    navController.popBackStack("profile", false)
                }
                .align(Alignment.TopStart)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .offset(y = 50.dp),
        ) {
            Image(
                painter = painterResource(selectedImage),
                contentDescription = "background",
                modifier = Modifier
                    .width(200.dp)
                    .height(240.dp)
                    .background(Color(0xFFFFFFFF), RoundedCornerShape(12.dp))
                    .border(3.dp, Color(0xFF00bb2f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 30.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(40.dp))
            Column(Modifier.fillMaxWidth(0.8f)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("이름") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
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
                                containerColor = Color(0xFFfefff3)
                            ),
                            modifier = Modifier
                                .height(75.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .padding(5.dp)
                        )
                        Box {
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
                                    containerColor = Color(0xFFfefff3)
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
                        AnimatedVisibility(
                            visible = showDatePicker,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Popup(
                                alignment = Alignment.TopStart
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 400.dp, height = 330.dp)
                                        .align(Alignment.CenterHorizontally)
                                        .offset(y = -10.dp)
                                        .padding(16.dp)
                                        .clip(RoundedCornerShape(16.dp))
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
                                        color = Color.Gray,
                                        thickness = 1.dp,
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .offset(y = -45.dp)
                                    )
                                    Button(
                                        onClick = { showDatePicker = false },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFFFFFF)
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
                                            containerColor = Color(0xFFFFFFFF)
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
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "만들기",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFFFF),
                        modifier = Modifier
                            .background(
                                Color(0xFF0e299c),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 15.dp, horizontal = 20.dp)
                            .clickable {
                                if (selectedDate.isAfter(LocalDate.now())) {
                                    Toast
                                        .makeText(context, "날짜를 잘못 입력하였습니다.", Toast.LENGTH_SHORT)
                                        .show()
                                } else when {
                                    name.isEmpty() -> {
                                        Toast
                                            .makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    // ★ 수정된 부분: 이름 유효성 검사 추가 ★
                                    !isValidName(name) -> {
                                        Toast
                                            .makeText(
                                                context,
                                                "이름은 한글 4자 이하 또는 영어 4음절 이하로 입력해주세요.",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                        name = ""
                                    }

                                    else -> {
                                        birth =
                                            birth.ifEmpty {
                                                LocalDate
                                                    .now()
                                                    .format(dateFormatter)
                                            }
                                        viewModel.addProfile(
                                            name,
                                            birth,
                                            userProfile++,
                                            selectedImage
                                        )
                                        navController.navigate("profile") {
                                            popUpTo("profile")
                                        }
                                    }
                                }
                            }
                    )
                }
                Row {
                    DynamicImageLoding { selectedImage = it }
                }
            }
        }
    }
}

// ★ 추가된 함수: 한글 4자, 영어 4음절 검사 ★
fun isValidName(name: String): Boolean {
    val trimmedName = name.trim()
    return if (trimmedName.matches(Regex("^[가-힣]+$"))) {
        trimmedName.length <= 4
    } else if (trimmedName.matches(Regex("^[A-Za-z]+$"))) {
        countSyllables(trimmedName) <= 4
    } else {
        false
    }
}

fun countSyllables(word: String): Int {
    val vowels = "aeiouy"
    var count = 0
    var lastVowel: Char? = null
    for (c in word.toLowerCase()) {
        if (c in vowels) {
            // 이전 모음이 없거나, 이전 모음과 다르면 음절 수 증가
            if (lastVowel == null || lastVowel != c) {
                count++
            }
            lastVowel = c
        } else {
            lastVowel = null
        }
    }
    // 단어가 "e"로 끝나고, 음절 수가 2 이상인 경우 예외 처리
    if (word.endsWith("e", ignoreCase = true) && count > 1) {
        count--
    }
    return count
}


@Composable
fun DynamicImageLoding(onImageSelected: (Int) -> Unit) {
    for (i in 1..4) {
        val resourceId = getDrawableResourceId("test$i")
        if (resourceId != 0) {
            Image(
                painter = painterResource(resourceId),
                contentDescription = "foreground",
                modifier = Modifier
                    .size(100.dp)
                    .border(3.dp, Color(0xFF00bb2f), RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFFFFF), RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .padding(15.dp)
                    .clickable {
                        onImageSelected(resourceId)
                    }
            )
        }
        Spacer(modifier = Modifier.width(30.dp))
    }
}

fun getDrawableResourceId(resourceName: String): Int {
    return try {
        val resourceId = R.drawable::class.java.getField(resourceName).getInt(null)
        resourceId
    } catch (e: Exception) {
        0
    }
}
