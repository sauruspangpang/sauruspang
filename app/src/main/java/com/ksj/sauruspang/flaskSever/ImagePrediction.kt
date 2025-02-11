package com.ksj.sauruspang.flaskSever

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ksj.sauruspang.Learnpackage.camera.DetectedResultListViewModel
import com.ksj.sauruspang.flaskSever.ImagePrediction.predictionResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

// ImageInput sealed class 정의
sealed class ImageInput {
    data class UriInput(val uri: Uri?) : ImageInput()
    data class BitmapInput(val bitmap: Bitmap?) : ImageInput()
}

object ImagePrediction {

    private const val SERVER_URL = "https://starfish-evolved-molly.ngrok-free.app/predict"
    private val client by lazy { OkHttpClient.Builder().build() }

    // 서버에서 예측된 prediction_result을 누적 저장할 리스트
    val predictionResults = mutableListOf<String>()

    /**
     * 하나의 함수로 Uri와 Bitmap 모두 처리
     * 1) ImageInput 타입에 따라 임시 File 생성
     * 2) Multipart로 서버에 전송
     * 3) JSON 응답 파싱 -> predictionResults 리스트 업데이트
     * 4) 콜백(onResult)으로 최신 리스트 전달
     */

    fun uploadImageToServer(
        context: Context,
        imageInput: ImageInput,
        selectedModel: String,
        onResult: (List<String>) -> Unit
    ) {
        // 새로운 요청마다 결과 리스트 초기화
        predictionResults.clear()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 이미지 입력 타입에 따라 임시 파일 생성
                val imageFile = when (imageInput) {
                    is ImageInput.UriInput -> createTempFileFromUri(context, imageInput.uri!!)
                    is ImageInput.BitmapInput -> createTempFileFromBitmap(context, imageInput.bitmap!!)
                }

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        name = "image",
                        filename = imageFile.name,
                        body = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                    .build()

                // 모델 선택은 URL의 쿼리 스트링으로 전달
                val request = Request.Builder()
                    .url("$SERVER_URL?model=$selectedModel")
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    val responseBody = response.body?.string()
                    if (response.isSuccessful && responseBody != null) {
                        Log.d("Flask", "서버 응답: $responseBody")
                        parseJsonAndStore(responseBody)
                    } else {
                        Log.e("Flask", "서버 에러: code=${response.code}, body=$responseBody")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Flask", "전송 실패: ${e.message}")
            }

            // 응답 파싱 후 결과 리스트 콜백 전달
            onResult(predictionResults.toMutableList())
        }
    }

    /**
     * JSON 파싱 -> prediction_result 추가
     */
    private fun parseJsonAndStore(responseBody: String) {
        try {
            val jsonObj = JSONObject(responseBody)
            val type = jsonObj.optString("type", "")
            val success = jsonObj.optBoolean("success", false)
            Log.d("Flask", "Parsed success=$success, type=$type")

            if (!success) {
                Log.e("Flask", "Server returned error or success=false")
                return
            }

            when (type) {
                "classification" -> {
                    jsonObj.optJSONArray("predictions")?.let { predictionsArray ->
                        for (i in 0 until predictionsArray.length()) {
                            val predObj = predictionsArray.getJSONObject(i)
                            val predictionResult = predObj.optString("prediction_result", "")
                            val confidence = predObj.optDouble("confidence", 0.0)
                            Log.d("Flask", "Classification #$i => prediction_result=$predictionResult, conf=$confidence")
                            if (predictionResult.isNotEmpty()) {
                                addIfNotDuplicate(predictionResult)
                            }
                        }
                    }
                }
                "detection" -> {
                    jsonObj.optJSONArray("detections")?.let { detectionsArray ->
                        for (i in 0 until detectionsArray.length()) {
                            val detObj = detectionsArray.getJSONObject(i)
                            val predictionResult = detObj.optString("prediction_result", "")
                            val conf = detObj.optDouble("confidence", 0.0)
                            Log.d("Flask", "Detection #$i => prediction_result=$predictionResult, conf=$conf")
                            if (predictionResult.isNotEmpty()) {
                                addIfNotDuplicate(predictionResult)
                            }
                        }
                    }
                }
                else -> {
                    Log.w("Flask", "Unknown type=$type, no prediction_result added.")
                }
            }

            Log.d("Flask", "predictionResults 리스트 => $predictionResults")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Flask", "JSON parse error: ${e.message}")
        }
    }

    // 중복 예측값이 없을 때만 리스트에 추가
    private fun addIfNotDuplicate(newClass: String) {
        if (!predictionResults.contains(newClass)) {
            predictionResults.add(newClass)

        }
    }

    // 이미지 타입 Uri -> .jpg 파일 생성
    private fun createTempFileFromUri(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Cannot open input stream from $uri")
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        copyStreamToFile(inputStream, tempFile)
        return tempFile
    }

    // 이미지 타입 Bitmap -> .jpg 파일 생성
    private fun createTempFileFromBitmap(context: Context, bitmap: Bitmap): File {
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        FileOutputStream(tempFile).use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
        }
        return tempFile
    }

    // InputStream의 내용을 파일에 복사
    private fun copyStreamToFile(inputStream: InputStream, file: File) {
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
    }
}

