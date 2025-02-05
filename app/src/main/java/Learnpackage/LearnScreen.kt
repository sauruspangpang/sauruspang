package Learnpackage

import ProfilePackage.ProfileViewmodel
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.R

@Composable
fun LearnScreen(navController: NavController, category: String, day: String) {
    val images = when (category) {
        "과일" -> listOf(R.drawable.fruit, R.drawable.fruit, R.drawable.fruit)
        "색깔" -> listOf(R.drawable.test1, R.drawable.test2, R.drawable.test3)
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFFDD4AA))
            .fillMaxSize()
    ) {
        Text(
            text = "$category - $day",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.padding(20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            images.forEach { image ->
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(10.dp)
                )
            }
        }
    }
}