package com.ksj.sauruspang.GptApi

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/***********************************************************************
 * 데이터 클래스: 파이썬 예시의 messages 구조와 동일하게 구성
 ***********************************************************************/

data class ChatCompletionRequest(
    val model: String,
    val messages: List<OpenAIMessage>,
    val temperature: Float? = null,
    @SerializedName("max_tokens") val maxTokens: Int? = null
)

/** messages[*] => role, content(배열) */
data class OpenAIMessage(
    val role: String,
    val content: List<OpenAIContent> // "content"가 [ {type, text}, {type, image_url} ] 구조
)

/** content[ i ] => type, text?, image_url? */
data class OpenAIContent(
    val type: String,
    val text: String? = null,
    val image_url: OpenAIImageUrl? = null
)

/** image_url: { url: "data:image/jpeg;base64,..." } */
data class OpenAIImageUrl(
    val url: String
)

/***********************************************************************
 * 응답 구조 (간단 버전)
 ***********************************************************************/
data class ChatCompletionResponse(
    val choices: List<Choice>?
)

data class Choice(
    val message: ChoiceMessage?
)

data class ChoiceMessage(
    val role: String?,
    val content: String?
)

/***********************************************************************
 * Retrofit 인터페이스 & Client
 ***********************************************************************/
interface SimpleApi {
    @POST("v1/chat/completions")
    suspend fun sendChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>
}

object GptRetrofitClient {
    private const val BASE_URL = "https://api.openai.com/"

    val api: SimpleApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SimpleApi::class.java)
    }
}
