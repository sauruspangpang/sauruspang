package Learnpackage

import ProfilePackage.ProfileViewmodel
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
fun StageScreen(navController: NavController, viewModel: ProfileViewmodel, category: String) {
    val items = listOf("Day1", "Day2", "Day3", "Day4", "Day5", "Day6", "Day7")
    Column(
        modifier = Modifier
            .background(Color(0xFFFDD4AA))
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ellipse_1),
                contentDescription = "",
            )
            Column() {
                Text(
                    "Hello 박민준",
                    modifier = Modifier
                        .padding(start = 20.dp),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )
                Text(
                    "⭐\uFE0F 245",
                    modifier = Modifier
                        .padding(start = 20.dp),
                )
            }
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.book),
                contentDescription = "",
                modifier = Modifier
                    .size(70.dp)
                    .clickable {
                        navController.navigate("Rcoginition")
                    }
            )

        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState())
        ) {
            ZigzagRow(items, category, navController)
        }


    }

}


@Composable
fun ZigzagRow(days: List<String>, category: String, navController: NavController) {
    Row(
        modifier = Modifier
            .padding(30.dp)
    ) {
        days.forEachIndexed { index, day ->
            DayBox(
                day = day,
                isTop = index % 2 == 0, // Alternate position (even index: bottom, odd index: top)
                category = category,
                navController = navController
            )
        }
    }
}

@Composable
fun DayBox(day: String, isTop: Boolean, category: String, navController: NavController) {
    Box(
        modifier = Modifier
            .offset(y = if (isTop) (-20).dp else 80.dp)
            .size(width = 140.dp, height = 90.dp)
            .background(Color.White)
            .clickable { navController.navigate("learn/$category/$day") },
        contentAlignment = Alignment.Center
    ) {
        Text(
            day,
            style = TextStyle(
                fontSize = 40.sp
            )
        )
    }
}