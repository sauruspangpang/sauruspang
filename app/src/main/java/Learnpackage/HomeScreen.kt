package Learnpackage

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.ksj.sauruspang.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

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
            )

        }
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ){
            CategoryBox(R.drawable.rectangle1,"차")
            CategoryBox(R.drawable.rectangle1,"차")
            CategoryBox(R.drawable.rectangle1,"차")
            CategoryBox(R.drawable.rectangle1,"차")
            CategoryBox(R.drawable.rectangle1,"차")
            CategoryBox(R.drawable.rectangle1,"차")
            CategoryBox(R.drawable.rectangle1,"차")

        }

    }



}


@Composable
fun CategoryBox(fruit: Int, name:String){
    Box(
        modifier = Modifier
            .fillMaxHeight()
//                    .background(Color.White)

    ) {
        Image(
            painter = painterResource(id = fruit),
            contentDescription = "",
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.Center)
        )
        Image(
            painter = painterResource(id = R.drawable.fruit),
            contentDescription = "",
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center)
        )
        Text(name,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
        )

        Image(
            painter = painterResource(id = R.drawable.camera),
            contentDescription = "",
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.TopEnd)
                .padding(top=10.dp,end=10.dp)
        )
    }
}




