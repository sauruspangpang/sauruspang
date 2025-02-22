package com.ksj.sauruspang.ProfilePackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("프로필 편집", fontSize = 20.sp) },
        text = {
            LazyColumn {
                items(profiles.size) { index ->
                    val profile = profiles[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(id = profile.selectedImage),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = profile.name,
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        Checkbox(
                            checked = selectedProfileId == profile.userprofile,
                            onCheckedChange = { checked ->
                                selectedProfileId = if (checked) profile.userprofile else null
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedProfileId?.let { id ->
                        val profileToDelete = profiles.find { it.userprofile == id }
                        if (profileToDelete != null) {
                            onDelete(profileToDelete)
                        }
                    }
                }
            ) {
                Text("삭제")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        },
        modifier = Modifier.background(Color.White)
    )
}
