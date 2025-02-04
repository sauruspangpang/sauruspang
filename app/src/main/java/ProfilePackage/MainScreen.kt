package ProfilePackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ksj.sauruspang.R


@Composable
fun MainScreen(navController: NavController, viewModel: ProfileViewmodel) {
    var name by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var userProfile by remember { mutableStateOf(0) }
    var selectedImage by remember { mutableStateOf(R.drawable.test1) }

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
                .clip(CircleShape)
        )
        Row {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.padding(10.dp)
                )
                TextField(
                    value = birth,
                    onValueChange = { birth = it },
                    modifier = Modifier.padding(10.dp)
                )
                Row {
                    DynamicImageLoding{selectedImage = it}
                }
            }
            Button(onClick = {
                if (name.isNotEmpty() && birth.isNotEmpty()) {
                    viewModel.addProfile(name, birth, userProfile++, selectedImage)
                }
                navController.navigate("profile")
            }, modifier = Modifier.padding(10.dp)) {
                Text(text = "만들기")

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