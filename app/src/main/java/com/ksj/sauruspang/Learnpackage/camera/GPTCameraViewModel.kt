package com.ksj.sauruspang.Learnpackage.camera

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksj.sauruspang.BuildConfig
import com.ksj.sauruspang.GptApi.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class GPTCameraViewModel : ViewModel() {

    /** 요청 결과를 담을 상태값 */
    var predictionResult = mutableStateOf("")

    /**
     * 1) Bitmap 압축/축소
     * 2) Base64 변환
     * 3) JSON (model, messages) 구성
     * 4) Retrofit 호출 → 응답 수신
     */
    fun analyzeImage(image: Bitmap) {
        viewModelScope.launch {
            try {
                // 1) 이미지 축소 & 압축 (가로/세로 300 이하, JPEG 품질 60%)
                val scaled = scaleDownBitmap(image, 300)
                val base64Image = bitmapToBase64(scaled, 60)

                // 2) messages[*].content = [
                //     {type:"text", text:"What is in this image?"},
                //     {type:"image_url", image_url:{url:"data:image/jpeg;base64,..."}} ]
                val contentList = listOf(
                    OpenAIContent(
                        type = "text",
                        text = "Please print the most important object in this photo in one word.\n" +
                                "Please print it in Korean as well.\n" +
                                "The output format is as follows:\n" +
                                "Korean,English"
                    ),
                    OpenAIContent(
                        type = "image_url",
                        image_url = OpenAIImageUrl(
                            url = "data:image/jpeg;base64,$base64Image"
                        )
                    )
                )

                val userMessage = OpenAIMessage(
                    role = "user",
                    content = contentList
                )

                // 3) ChatCompletionRequest
                val requestBody = ChatCompletionRequest(
                    model = "gpt-4o-mini",
                    messages = listOf(userMessage),
                    maxTokens = 50,        // 필요에 따라 조절
                    temperature = 0.0f     // 필요에 따라 조절
                )

                // 4) Retrofit API 호출
                val response = GptRetrofitClient.api.sendChatCompletion(
                    "Bearer ${BuildConfig.API_KEY}",
                    requestBody
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    val result = body?.choices?.firstOrNull()?.message?.content ?: "(No content)"
                    predictionResult.value = result
                    Log.d("GPTCameraViewModel", "Success! content=$result")
                } else {
                    val code = response.code()
                    val errorBody = response.errorBody()?.string() ?: "(no error body)"
                    predictionResult.value = "Error: $code\n$errorBody"
                    Log.e("GPTCameraViewModel", "Response failed. code=$code, body=$errorBody")
                }

            } catch (e: Exception) {
                predictionResult.value = "Exception: ${e.localizedMessage}"
                Log.e("GPTCameraViewModel", "analyzeImage error", e)
            }
        }
    }

    /**
     * 가로/세로 중 긴 쪽을 maxSide로 맞춰 축소
     */
    private fun scaleDownBitmap(original: Bitmap, maxSide: Int): Bitmap {
        val width = original.width
        val height = original.height
        if (width <= maxSide && height <= maxSide) {
            return original
        }
        val ratio = if (width > height) {
            maxSide.toFloat() / width
        } else {
            maxSide.toFloat() / height
        }
        val newW = (width * ratio).toInt()
        val newH = (height * ratio).toInt()
        return Bitmap.createScaledBitmap(original, newW, newH, true)
    }

    /**
     * Bitmap → Base64 (JPEG)
     */
    private fun bitmapToBase64(bitmap: Bitmap, quality: Int): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val bytes = stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}
