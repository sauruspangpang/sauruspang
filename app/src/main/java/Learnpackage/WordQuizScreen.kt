package Learnpackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ksj.sauruspang.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordQuizScreen(avController: NavController, category: String, day: String) {
    var progress by remember { mutableFloatStateOf(0.2f) } // Example progress (50%)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "",
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { avController.popBackStack() }
                        )
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(20.dp)
                                .align(Alignment.Center)
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFDD4AA)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFDD4AA))

        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "",
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterStart)
            )
            Image(
                painter = painterResource(id = R.drawable.book),
                contentDescription = "",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            )
            Image(
                painter = painterResource(id = R.drawable.frontnull),
                contentDescription = "",
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { avController.navigate("") }
            )
        }
    }

}

