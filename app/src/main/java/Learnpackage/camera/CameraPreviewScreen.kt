package Learnpackage.camera

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraPreviewScreen(onImageCaptured: (Bitmap) -> Unit) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        val camera =
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        Button(
            onClick = {
                captureImage(imageCapture, context) { bitmap ->
                    onImageCaptured(bitmap) // 캡처된 이미지를 콜백을 통해 전달
                }
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp)
                .size(80.dp)
        ) {}
    }
}


fun captureImage(imageCapture: ImageCapture, context: Context, onImageCaptured: (Bitmap) -> Unit) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = image.toBitmap()
                onImageCaptured(bitmap)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                println("사진 캡처 실패: $exception")
            }
        }
    )
}

// ImageProxy를 Bitmap으로 변환하는 확장 함수
fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}



suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { continuation ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                continuation.resume(cameraProviderFuture.get())
            },
            ContextCompat.getMainExecutor(this)
        )
    }