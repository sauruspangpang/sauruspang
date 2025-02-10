package com.ksj.sauruspang.Learnpackage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ksj.sauruspang.Learnpackage.QuizCategory.Companion.allCategories
import com.ksj.sauruspang.ProfilePackage.ProfileViewmodel
import com.ksj.sauruspang.R
import java.io.OutputStream

@Composable
fun PictorialBookScreen(
    navController: NavController,
    categoryName: String,
    viewModel: ProfileViewmodel
) {
    val currentProfile = viewModel.profiles.find { it.category == categoryName }
    val categories = allCategories.map { it.name }
    var selectedCategory by remember {
        mutableStateOf(currentProfile?.quizCategory ?: categories.first())
    }
    var selectedIndex = categories.indexOf(selectedCategory).coerceAtLeast(0)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.choosecategory_wallpaper),
            contentDescription = "배경 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_backhome),
                    contentDescription = "카테고리 선택으로 이동",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate("home")
                        }
                )
                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
                    edgePadding = 0.dp,
                    modifier = Modifier.wrapContentWidth(),
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ) {
                    categories.forEachIndexed { index, category ->
                        Tab(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedCategory = category
                                selectedIndex = index
                            }
                        ) {
                            Text(category, modifier = Modifier.padding(16.dp))
                        }
                    }
                }
                PhotoGrid(selectedCategory)
            }
        }
    }
}

@Composable
fun PhotoGrid(category: String) {
    val context = LocalContext.current
    val photos = remember { loadPhotos(context, category) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(photos) { photoUri ->
            Image(
                painter = rememberAsyncImagePainter(photoUri),
                contentDescription = "User Photo",
                modifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )
        }
    }
}

fun loadPhotos(context: Context, category: String): List<Uri> {
    val photos = mutableListOf<Uri>()

    val projection = arrayOf(MediaStore.Images.Media._ID)
    val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
    val selectionArgs = arrayOf(category)
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val cursor: Cursor? =
        context.contentResolver.query(queryUri, projection, selection, selectionArgs, sortOrder)

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val photoUri =
                Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
            photos.add(photoUri)
        }
    }

    return photos
}
//}
//                CustomTabRow(
//                    categories = categories,
//                    selectedIndex = selectedIndex,
//                    onCategorySelected = { index ->
//                        selectedIndex = index
//                        selectedCategory = categories[index] // 선택한 카테고리 반영
//                    }
//                )
//            }
//        }
//    }
//}
//


//    fun savePhoto(context: Context, bitmap: Bitmap, category: String): Uri? {
//        val contentResolver = context.contentResolver
//        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
//        } else {
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        }
//
//        val contentValues = ContentValues().apply {
//            put(
//                MediaStore.Images.Media.DISPLAY_NAME,
//                "IMG_${System.currentTimeMillis()}.jpg"
//            ) // 파일명
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // 파일 타입
//            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$category/") // 저장 경로 (앨범명)
//            put(MediaStore.Images.Media.IS_PENDING, 1) // 저장 중 상태
//        }
//
//        return try {
//            val imageUri = contentResolver.insert(imageCollection, contentValues)
//            imageUri?.let { uri ->
//                val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
//                outputStream?.use { stream ->
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) // 이미지 저장
//                }
//                contentValues.clear()
//                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0) // 저장 완료
//                contentResolver.update(uri, contentValues, null, null)
//            }
//            imageUri
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }


//    // ScrollableTabRow 말고 다른거 쓸때 사용하는 CustomTabRow
//    @Composable
//    fun CustomTabRow(
//        categories: List<String>,
//        selectedIndex: Int,
//        onCategorySelected: (Int) -> Unit
//    ) {
//        LazyRow(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Start
//        ) {
//            items(categories.size) { index ->
//                val category = categories[index]
//                Box(
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp, vertical = 4.dp)
//                        .clickable { onCategorySelected(index) }
//                        .background(if (selectedIndex == index) Color.LightGray else Color.Transparent)
//                ) {
//                    Text(
//                        text = category,
//                        modifier = Modifier.padding(16.dp),
//                        color = if (selectedIndex == index) Color.Black else Color.DarkGray
//                    )
//                }
//            }
//        }
//    }

