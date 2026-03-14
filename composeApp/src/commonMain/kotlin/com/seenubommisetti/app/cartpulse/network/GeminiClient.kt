package com.seenubommisetti.app.cartpulse.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class GeminiClient(
    private val apiKey: String,
    private val model: String = DEFAULT_MODEL,
    private val httpClient: HttpClient = createHttpClient(),
) : AiClient {

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    override suspend fun generateReply(
        systemInstruction: String,
        conversation: List<AiConversationTurn>,
    ): String {
        if (apiKey.isBlank()) {
            throw GeminiClientException(
                "Gemini API key is missing. Add GEMINI_API_KEY to local.properties."
            )
        }

        val request = GeminiGenerateContentRequest(
            systemInstruction = GeminiContent(
                parts = listOf(GeminiPart(text = systemInstruction))
            ),
            contents = conversation.map { turn ->
                GeminiContent(
                    role = turn.role.toGeminiRole(),
                    parts = listOf(GeminiPart(text = turn.text)),
                )
            }
        )

        return try {
            val response = httpClient.post("$BASE_URL/models/$model:generateContent") {
                header("x-goog-api-key", apiKey)
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.bodyAsText()

            if (!response.status.isSuccess()) {
                throw GeminiClientException(extractGeminiErrorMessage(responseBody, response.status.value))
            }

            val completion = try {
                json.decodeFromString<GeminiGenerateContentResponse>(responseBody)
            } catch (error: SerializationException) {
                throw GeminiClientException(
                    "Gemini returned an unexpected response. Check the model and request body.",
                    error,
                )
            }

            completion.candidates
                .asSequence()
                .flatMap { candidate -> candidate.content.parts.orEmpty().asSequence() }
                .mapNotNull { it.text?.trim() }
                .firstOrNull { it.isNotEmpty() }
                ?: throw GeminiClientException("Gemini returned an empty response.")
        } catch (error: IOException) {
            throw GeminiClientException("Network error while contacting Gemini. Please try again.", error)
        }
    }

    companion object {
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta"
        private const val DEFAULT_MODEL = "gemini-2.5-flash"

        private fun createHttpClient(): HttpClient = HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    }
                )
            }
        }
    }
}

class GeminiClientException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

private fun AiConversationTurn.Role.toGeminiRole(): String = when (this) {
    AiConversationTurn.Role.User -> "user"
    AiConversationTurn.Role.Assistant -> "model"
}

private fun extractGeminiErrorMessage(
    responseBody: String,
    statusCode: Int,
): String {
    val json = Json { ignoreUnknownKeys = true }
    val parsedMessage = runCatching {
        json.decodeFromString<GeminiErrorEnvelope>(responseBody).error?.message
    }.getOrNull()

    return parsedMessage ?: "Gemini request failed with HTTP $statusCode."
}
