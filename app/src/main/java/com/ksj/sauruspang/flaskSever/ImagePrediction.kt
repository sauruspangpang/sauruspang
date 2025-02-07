package com.ksj.sauruspang.flaskSever

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
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

    private const val SERVER_URL = "http://192.168.45.54:6945/predict"
    private val client by lazy { OkHttpClient.Builder().build() }

    // 서버에서 예측된 prediction_result을 누적 저장할 리스트
    private val predictionResults = mutableListOf<String>()

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
        selectedModel: String, // TODO String 또는 Model enum 사용 가능
        onResult: (List<String>) -> Unit
    ) {
        // (1) 새로운 업로드 요청마다 리스트 초기화
        predictionResults.clear()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // imageInput에 따라 임시 파일 생성
                val imageFile = when (imageInput) {
                    is ImageInput.UriInput -> createTempFileFromUri(context, imageInput.uri!!)
                    is ImageInput.BitmapInput -> createTempFileFromBitmap(
                        context,
                        imageInput.bitmap!!
                    )
                }

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        name = "image",
                        filename = imageFile.name,
                        body = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                    .addFormDataPart(
                        name = "model",
                        value = selectedModel // TODO selectedModel == 모델 선택
                    )
                    .build()

                val request = Request.Builder()
                    .url(SERVER_URL)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    Log.d("Flask", "서버 응답: $responseBody")
                    parseJsonAndStore(responseBody)
                } else {
                    Log.e("Flask", "서버 에러: code=${response.code}, body=$responseBody")
                }

                response.close()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Flask", "전송 실패: ${e.message}")
            }

            // 서버 응답 파싱을 마친 뒤, 최신 리스트를 콜백으로 전달
            onResult(predictionResults.toList())
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

            // 로깅
            Log.d("Flask", "Parsed success=$success, type=$type")

            if (!success) {
                // 실패 응답이면 early return
                Log.e("Flask", "Server returned error or success=false")
                return
            }

            if (type == "classification") {
                // 분류 모델
                val predictionResult = jsonObj.optString("prediction_result", "")
                val confidence = jsonObj.optDouble("confidence", 0.0)

                // 로그
                Log.d(
                    "Flask",
                    "Classification: prediction_result=$predictionResult, conf=$confidence"
                )

                // 저장
                if (predictionResult.isNotEmpty()) {
                    addIfNotDuplicate(predictionResult)
                }

            } else if (type == "detection") {
                // 객체 감지 모델
                val detectionsArray = jsonObj.optJSONArray("detections")

                // detectionsArray 순회
                if (detectionsArray != null) {
                    for (i in 0 until detectionsArray.length()) {
                        val detObj = detectionsArray.getJSONObject(i)
                        val predictionResult = detObj.optString("prediction_result", "")
                        val conf = detObj.optDouble("confidence", 0.0)
                        // bounding_box_area 등도 필요하면 파싱 가능

                        // 로그
                        Log.d(
                            "Flask",
                            "Detection #$i => prediction_result=$predictionResult, conf=$conf"
                        )

                        // 원하는 방식으로 저장
                        if (predictionResult.isNotEmpty()) {
                            addIfNotDuplicate(predictionResult)
                        }
                    }
                }
            } else {
                // 다른 타입일 경우
                Log.w("Flask", "Unknown type=$type, no prediction_result added.")
            }

            // 최종 리스트 로그
            Log.d("Flask", "predictionResults 리스트 => $predictionResults")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Flask", "JSON parse error: ${e.message}")
        }
    }

    // 중복 예측값 제거
    private fun addIfNotDuplicate(newClass: String) {
        if (!predictionResults.contains(newClass)) {
            predictionResults.add(newClass)
        }
    }

    // 이미지 타입 Uri -> .jpg
    private fun createTempFileFromUri(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Cannot open input stream from $uri")
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        copyStreamToFile(inputStream, tempFile)
        return tempFile
    }

    // 이미지 타입 Bitmap -> .jpg
    private fun createTempFileFromBitmap(context: Context, bitmap: Bitmap): File {
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        FileOutputStream(tempFile).use { output ->
            // JPEG 형식으로 압축 (품질 90%)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
        }
        return tempFile
    }

    private fun copyStreamToFile(inputStream: InputStream, file: File) {
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
    }
}
