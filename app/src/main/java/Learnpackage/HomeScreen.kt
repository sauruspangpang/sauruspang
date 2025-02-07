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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
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

    Column(
        modifier = Modifier
            .background(Color(0xFFFDD4AA))
            .fillMaxSize()
            .paint(painterResource(R.drawable.main_wallpaper))
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
                .horizontalScroll(rememberScrollState())
        ) {
            
            QuizCategory.allCategories.forEach { category ->
                CategoryBox(category,navController)
            }

        }

    }


}


@Composable
fun CategoryBox(category: QuizCategory, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clickable {navController.navigate("stage/${category.name}")  }

    ) {
        Image(
            painter = painterResource(R.drawable.rectangle1),
            contentDescription = "",
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.Center)
               // .clickable { navController.navigate("stage") }
        )
        Image(
            painter = painterResource(id = category.thumbnail),
            contentDescription = "",
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center)
        )
        Text(
            category.name,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
        )

        Image(
            painter = painterResource(id = R.drawable.image_cameramark),
            contentDescription = "",
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 10.dp)
            //    .clickable { navController.navigate("stage") }
        )
    }
}




