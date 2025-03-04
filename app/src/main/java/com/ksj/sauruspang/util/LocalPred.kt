package com.ksj.sauruspang.util

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.nio.FloatBuffer
import kotlin.math.max
import kotlin.math.min

val classificationClassMap = mapOf(
    0 to "black",
    1 to "blue",
    2 to "gray",
    3 to "green",
    4 to "navy",
    5 to "orange",
    6 to "purple",
    7 to "red",
    8 to "white",
    9 to "yellow"
)

/**
 * 바운딩 박스 데이터 클래스.
 * @param x1 왼쪽 상단 x좌표
 * @param y1 왼쪽 상단 y좌표
 * @param x2 오른쪽 하단 x좌표
 * @param y2 오른쪽 하단 y좌표
 * @param confidence 탐지 확률
 * @param classId 클래스 인덱스
 */
data class DetectionBox(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
    val confidence: Float,
    val classId: Int
)

object ImagePrediction {

    /**
     * 원본 Bitmap에서 짧은 변을 기준으로 1:1 중앙 크롭한 뒤, 원하는 크기로 리사이즈합니다.
     * 파이썬의 crop_and_resize()와 유사한 로직.
     */
    private fun cropAndResizeShortSide(
        sourceBitmap: Bitmap,
        outputWidth: Int,
        outputHeight: Int
    ): Bitmap {
        val w = sourceBitmap.width
        val h = sourceBitmap.height
        val side = min(w, h)
        val centerX = w / 2
        val centerY = h / 2
        val halfSide = side / 2

        val startX = max(centerX - halfSide, 0)
        val startY = max(centerY - halfSide, 0)

        val cropped = Bitmap.createBitmap(sourceBitmap, startX, startY, side, side)
        return Bitmap.createScaledBitmap(cropped, outputWidth, outputHeight, true)
    }

    /**
     * 원본 Bitmap에서 (1/3 ~ 2/3) 중앙 영역만 크롭 후, 원하는 크기로 리사이즈합니다.
     * 파이썬의 classify_center_region()에 대응되는 로직.
     */
    private fun cropCenterRegion(
        sourceBitmap: Bitmap,
        targetWidth: Int,
        targetHeight: Int
    ): Bitmap {
        val w = sourceBitmap.width
        val h = sourceBitmap.height

        val left = w / 3
        val right = (2 * w) / 3
        val top = h / 3
        val bottom = (2 * h) / 3

        val cropWidth = right - left
        val cropHeight = bottom - top

        val cropped = Bitmap.createBitmap(sourceBitmap, left, top, cropWidth, cropHeight)
        return Bitmap.createScaledBitmap(cropped, targetWidth, targetHeight, true)
    }

    /**
     * YOLO 계열 onnx 모델을 사용해 객체 탐지를 수행하는 예시 함수.
     *
     * @param context Context
     * @param bitmap 원본 Bitmap
     * @param modelFileName assets/ 혹은 내부 저장소의 onnx 모델 파일 이름
     * @param confThreshold confidence 임계값(기본 0.25)
     * @param iouThreshold NMS 시 사용될 IOU 임계값(기본 0.45). 현재는 미구현, TODO.
     * @return DetectionBox 리스트
     */
    fun detectionONNX(
        context: Context,
        bitmap: Bitmap,
        modelFileName: String,
        confThreshold: Float = 0.25f,
        iouThreshold: Float = 0.45f
    ): List<DetectionBox> {
        val detectionResults = mutableListOf<DetectionBox>()
        var session: OrtSession? = null
        var onnxEnv: OrtEnvironment? = null

        try {
            onnxEnv = OrtEnvironment.getEnvironment()
            session = onnxEnv.createSession(
                assetFilePath(context, modelFileName),
                OrtSession.SessionOptions()
            )

            // 640x640 리사이즈
            val inputWidth = 640
            val inputHeight = 640
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputWidth, inputHeight, true)

            // 텐서 생성: [1, 3, H, W]
            val floatBuffer = convertBitmapToFloatBuffer(resizedBitmap, inputWidth, inputHeight)
            val inputShape = longArrayOf(1, 3, inputHeight.toLong(), inputWidth.toLong())
            val inputName = session.inputNames.iterator().next()
            val inputTensor = OnnxTensor.createTensor(onnxEnv, floatBuffer, inputShape)

            // 모델 추론
            val results = session.run(mapOf(inputName to inputTensor))
            val outputName = session.outputNames.iterator().next()

            // OnnxValue 안전 취득
            val onnxValue = results.get(outputName).orElse(null)
            if (onnxValue != null) {
                val rawOutput = onnxValue.value
                Log.d("OnnxDebug", "ONNX 원본 출력: $rawOutput")

                if (rawOutput is Array<*> && rawOutput.isNotEmpty()) {
                    // ex) [1, 14, 8400] => Array<Array<FloatArray>>
                    val firstDim = rawOutput[0]
                    if (firstDim is Array<*>) {
                        val outputData = firstDim as Array<FloatArray>  // shape=[14, 8400]
                        val featureCount = outputData.size    // 14
                        val predictionCount = outputData[0].size  // 8400

                        for (i in 0 until predictionCount) {
                            val x = outputData[0][i]
                            val y = outputData[1][i]
                            val w = outputData[2][i]
                            val h = outputData[3][i]

                            // 클래스별 점수에서 최대값 찾기
                            var maxScore = outputData[4][i]
                            var maxClassIdx = 0
                            for (c in 5 until featureCount) {
                                if (outputData[c][i] > maxScore) {
                                    maxScore = outputData[c][i]
                                    maxClassIdx = c - 4
                                }
                            }

                            // confidence threshold
                            if (maxScore >= confThreshold) {
                                val x1 = x
                                val y1 = y
                                val x2 = x + w
                                val y2 = y + h
                                detectionResults.add(
                                    DetectionBox(
                                        x1,
                                        y1,
                                        x2,
                                        y2,
                                        maxScore,
                                        maxClassIdx
                                    )
                                )
                            }
                        }
                    } else if (firstDim is FloatArray) {
                        // [1, N, 6] 구조라면 여기서 처리
                        // TODO: 구현 필요 시 확장
                    }
                }
                onnxValue.close()
            }

            // TODO: 필요하다면 여기서 NMS(Non-Max Suppression) 적용
            // detectionResults = applyNMSIfNeeded(detectionResults, iouThreshold)

            // 클래스 이름 매핑 (여기서는 10개 클래스 가정)
            val classNames = arrayOf(
                "Class0", "Class1", "Class2", "Class3", "Class4",
                "Class5", "Class6", "Class7", "Class8", "Class9"
            )

            // JSON 변환 예시
            val jsonArray = org.json.JSONArray()
            for (det in detectionResults) {
                val clsId = det.classId
                val clsName = if (clsId in classNames.indices) classNames[clsId] else "Unknown"
                val obj = org.json.JSONObject()
                obj.put("class_id", clsId)
                obj.put("class_name", clsName)
                obj.put("confidence", det.confidence)
                obj.put("bbox", org.json.JSONArray(listOf(det.x1, det.y1, det.x2, det.y2)))
                jsonArray.put(obj)
            }
            Log.d("OnnxLocal", "Detection JSON: $jsonArray")

            // 디버그용 바운딩 박스 이미지 저장
            val debugBitmap = resizedBitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(debugBitmap)
            val paint = Paint().apply {
                color = Color.RED
                style = Paint.Style.STROKE
                strokeWidth = 3f
            }
            for (det in detectionResults) {
                canvas.drawRect(det.x1, det.y1, det.x2, det.y2, paint)
            }
            val debugFile = File(context.getExternalFilesDir(null), "debug_detection.png")
            FileOutputStream(debugFile).use { out ->
                debugBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Log.d("OnnxLocal", "Saved debug image to: ${debugFile.absolutePath}")

            // 자원 해제
            results.close()
            inputTensor.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("OnnxLocal", "Local detection error: ${e.message}")
        } finally {
            // 세션/환경 닫기
            session?.close()
            onnxEnv?.close()
        }
        return detectionResults
    }

    /**
     * 분류 모델용 ONNX 런타임 예시 함수.
     * 파이썬의 classify_center_region()와 유사하게, 중앙 크롭 후 (224,224) 전처리 가능.
     *
     * @param context Context
     * @param bitmap 원본 이미지
     * @param modelFileName onnx 모델 파일 이름
     * @param useCenterRegion true면 중앙부 크롭, false면 짧은 변 1:1 크롭
     * @param threshold confidence 임계값
     * @param topK 상위 k개 결과 추출
     * @return (예측 클래스명, confidence) 쌍 리스트
     */
    fun classificationONNX(
        context: Context,
        bitmap: Bitmap,
        modelFileName: String,
        useCenterRegion: Boolean = true,
        threshold: Float = 0.2f,
        topK: Int = 5
    ): List<Pair<String, Float>> {

        val classificationResults = mutableListOf<Pair<String, Float>>()
        var session: OrtSession? = null
        var onnxEnv: OrtEnvironment? = null

        try {
            onnxEnv = OrtEnvironment.getEnvironment()
            session = onnxEnv.createSession(
                assetFilePath(context, modelFileName),
                OrtSession.SessionOptions()
            )

            val inputWidth = 224
            val inputHeight = 224

            // 중앙 크롭 / 짧은 변 크롭
            val processedBitmap = if (useCenterRegion) {
                cropCenterRegion(bitmap, inputWidth, inputHeight)
            } else {
                cropAndResizeShortSide(bitmap, inputWidth, inputHeight)
            }

            // 텐서 생성
            val floatBuffer = convertBitmapToFloatBuffer(processedBitmap, inputWidth, inputHeight)
            val inputShape = longArrayOf(1, 3, inputHeight.toLong(), inputWidth.toLong())
            val inputName = session.inputNames.iterator().next()
            val inputTensor = OnnxTensor.createTensor(onnxEnv, floatBuffer, inputShape)

            // 추론 실행
            val results = session.run(mapOf(inputName to inputTensor))
            val outputName = session.outputNames.iterator().next()

            // OnnxValue 안전 취득
            val onnxValue = results.get(outputName).orElse(null)
            if (onnxValue != null) {
                val raw = onnxValue.value
                if (raw is Array<*> && raw.isNotEmpty()) {
                    val array2D = raw as? Array<FloatArray>
                    if (array2D != null && array2D.isNotEmpty()) {
                        val scores = array2D[0]  // shape=[num_classes]
                        val sortedIndices = scores.indices.sortedByDescending { scores[it] }
                        val topIndices = sortedIndices.take(topK)
                        for (idx in topIndices) {
                            val conf = scores[idx]
                            if (conf >= threshold) {
                                val predictedClass = classificationClassMap[idx] ?: "class_$idx"
                                classificationResults.add(predictedClass to conf)
                            }
                        }
                    }
                }
                onnxValue.close()
            }

            results.close()
            inputTensor.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("OnnxLocal", "Local inference error: ${e.message}")
        } finally {
            // 세션/환경 닫기
            session?.close()
            onnxEnv?.close()
        }

        return classificationResults
    }

    /**
     * 모델 파일 경로를 반환.
     * assets/~ 에서 복사해 오는 로직을 포함.
     */
    private fun assetFilePath(context: Context, assetName: String): String {
        val file = File(context.filesDir, assetName)

        if (!file.exists()) {
            try {
                context.assets.open(assetName).use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d("OnnxDebug", "ONNX 모델 복사 성공: ${file.absolutePath}")
            } catch (e: Exception) {
                Log.e("OnnxError", "ONNX 모델 복사 실패: ${e.message}")
            }
        } else {
            Log.d("OnnxDebug", "ONNX 모델 이미 존재: ${file.absolutePath}")
        }
        return file.absolutePath
    }

    /**
     * Bitmap -> FloatBuffer 변환 (RGB 순서, /255.0 스케일링)
     * 생성된 텐서는 [1,3,H,W] 형태로 사용 가능.
     */
    private fun convertBitmapToFloatBuffer(
        bitmap: Bitmap,
        width: Int,
        height: Int
    ): FloatBuffer {
        val floatBuffer = FloatBuffer.allocate(1 * 3 * width * height)
        floatBuffer.rewind()

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)

                val r = Color.red(pixel) / 255.0f
                val g = Color.green(pixel) / 255.0f
                val b = Color.blue(pixel) / 255.0f

                floatBuffer.put(r)
                floatBuffer.put(g)
                floatBuffer.put(b)

                if (x == 0 && y < 5) {  // 첫 5줄만 로그 출력
                    Log.d("OnnxDebug", "입력 픽셀($x, $y): R=$r, G=$g, B=$b")
                }
            }
        }

        floatBuffer.rewind()
        return floatBuffer
    }

    /*
    // (선택) NMS 예시 함수: 필요 시 적용
    private fun applyNMSIfNeeded(
        detectionBoxes: List<DetectionBox>,
        iouThreshold: Float
    ): List<DetectionBox> {
        // TODO: NMS 구현
        return detectionBoxes
    }
    */
}
