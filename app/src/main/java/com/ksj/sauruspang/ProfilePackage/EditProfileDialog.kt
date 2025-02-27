package com.ksj.sauruspang.ProfilePackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ksj.sauruspang.R
import com.ksj.sauruspang.ProfilePackage.Profile

@Composable
fun EditProfileDialog(
    profiles: List<Profile>,
    onDismiss: () -> Unit,
    onDelete: (Profile) -> Unit
) {
    // 단일 선택 상태: 현재 선택된 프로필의 userprofile 값을 저장 (없으면 null)
    var selectedProfileId by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.8f))
            // 배경 클릭 시 다이얼로그 외부 터치로 dismiss 처리 (내부 Box는 클릭 이벤트 전파 차단)
//            .clickable { onDismiss() }
    ) {
        // 중앙 다이얼로그 영역 (요청사항 1, 2 적용)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.7f)
        ) {
            // 다이얼로그 메인 컨텐츠 박스
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFFFEFFF3), shape = RoundedCornerShape(15.dp))
                    .border(width = 2.dp, color = Color(0xFF00BB2F), shape = RoundedCornerShape(15.dp))
                    .padding(horizontal = 16.dp)
                    // 내부 클릭 시 배경 클릭 이벤트 전파 방지
                    .clickable(enabled = false) {}
            ) {
                // 프로필 목록: 각 항목은 요청사항 3, 6 적용
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    profiles.forEach { profile ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f) // 화면의 일정 비율을 할당
                                    .background(Color(0xFFededed), shape = RoundedCornerShape(15.dp))
                                    .padding(12.dp)
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    // 1번째 줄: 아이콘과 이름
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = profile.selectedImage),
                                            contentDescription = "Profile Image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = profile.name,
                                            fontSize = 16.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    // 2번째 줄: 별 아이콘과 점수
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.image_star),
                                            contentDescription = "Star"
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = profile.score.toString(),
                                            fontSize = 16.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    // 3번째 줄: "- 카테고리" 텍스트
                                    Text(
                                        text = "- 카테고리",
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    // 4번째 줄: 학습 정보 (categoryDayStatus)
                                    Text(
                                        text = profile.categoryDayStatus.toString(),
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Checkbox(
                                checked = selectedProfileId == profile.userprofile,
                                onCheckedChange = { checked ->
                                    selectedProfileId = if (checked) profile.userprofile else null
                                },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = Color(0xFF00BB2F),
                                    checkmarkColor = Color(0xFF00BB2F), // 필요에 따라 수정
                                    checkedColor = Color(0xFF00BB2F)    // 필요에 따라 수정
                                ),
                                modifier = Modifier.align(Alignment.Top)
                                    .size(32.dp),

                            )
                            Spacer(modifier = Modifier.width(20.dp))

                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            // X 버튼: 다이얼로그 박스 오른쪽 위에 걸치게 (요청사항 5)
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 24.dp, y = (-24).dp)
            ) {
                Text(
                    text = "X",
                    modifier = Modifier
                        .background(
                            color = Color(0xFFd80000),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = Color(0xFFa3a3a3),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                )
            }

            // 프로필 삭제 버튼: 다이얼로그 박스 오른쪽 아래 외부에 배치 (요청사항 5)
            Text(
                text = "프로필 삭제",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 5.dp, y = 50.dp)
                    .background(
                        color = Color(0xFF00BB2F),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color(0xFFFEFFF3),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .clickable {
                        selectedProfileId?.let { id ->
                            val profileToDelete = profiles.find { it.userprofile == id }
                            if (profileToDelete != null) {
                                onDelete(profileToDelete)
                            }
                        }
                    },
                color = Color(0xFFFEFFF3),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
