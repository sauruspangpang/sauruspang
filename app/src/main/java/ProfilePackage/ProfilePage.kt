package ProfilePackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun ProfilePage(navController: NavController, viewModel: ProfileViewmodel) {
    Text(
        text = "사용자 프로필",
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues())
    )
    // 프로필 리스트
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        viewModel.profiles.forEach { profile ->
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                    Image(
                        painter = painterResource(profile.selectedImage),
                        contentDescription = "profile image",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate("home")
                            }
                    )
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "이름: ${profile.name}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "생년월일: ${profile.birth}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))

        // 새 프로필 추가 버튼
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "ADD",
            modifier = Modifier
                .padding(10.dp)
                .size(50.dp)
                .clip(CircleShape)
                .clickable { navController.navigate("main") } // 새 프로필 추가 화면으로 이동
        )
    }
}