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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ksj.sauruspang.R

@Composable
fun StageScreen(navController: NavController, categoryName: String, viewModel: ProfileViewmodel) {
    val category = QuizCategory.allCategories.find { it.name == categoryName }

    Column(
        modifier = Modifier
            .background(Color(0xFFFDD4AA))
            .fillMaxSize()
            .paint(painterResource(R.drawable.day_wallpaper))
    ) {
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
                        navController.navigate("home")
                    }
            )
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
                Image(painter = painterResource(R.drawable.image_starpoint), contentDescription = "")
                Text(
                    "245",
                    modifier = Modifier
                        .padding(start = 20.dp),
                )
            }
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.image_photobook),
                contentDescription = "",
                modifier = Modifier
                    .size(70.dp)
            )

        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .horizontalScroll(rememberScrollState())
        ){
            category?.days?.let { days ->
                ZigzagRow(days, categoryName, navController)
            }
        }


    }

}



@Composable
fun ZigzagRow(days: List<QuizDay>, categoryName: String, navController: NavController) {
    Row(
        modifier = Modifier
            .padding(30.dp)

    ) {
        days.forEachIndexed { index, day ->
            DayBox(
                dayIndex = day.dayNumber-1,
                isTop = index % 2 == 0,
                categoryName = categoryName,
                navController = navController
            )
        }
    }
}

@Composable
fun DayBox(dayIndex: Int, isTop: Boolean, categoryName: String, navController: NavController) {
    Box(
        modifier = Modifier
            .offset(y = if (isTop) (-20).dp else 80.dp)
            .size(width = 140.dp, height = 90.dp)
            .background(Color.White)
            .clickable {
                navController.navigate("learn/$categoryName/$dayIndex")
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Day ${dayIndex+1}",
            style = TextStyle(fontSize = 40.sp)
        )
    }
}