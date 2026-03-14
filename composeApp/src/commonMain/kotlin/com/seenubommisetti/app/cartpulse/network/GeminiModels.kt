package com.seenubommisetti.app.cartpulse.network

import kotlinx.serialization.Serializable

@Serializable
data class GeminiGenerateContentRequest(
    val systemInstruction: GeminiContent? = null,
    val contents: List<GeminiContent>,
)

@Serializable
data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>,
)

@Serializable
data class GeminiPart(
    val text: String? = null,
)

@Serializable
data class GeminiGenerateContentResponse(
    val candidates: List<GeminiCandidate> = emptyList(),
)

@Serializable
data class GeminiCandidate(
    val content: GeminiCandidateContent = GeminiCandidateContent(),
)

@Serializable
data class GeminiCandidateContent(
    val parts: List<GeminiPart>? = null,
)

@Serializable
data class GeminiErrorEnvelope(
    val error: GeminiErrorPayload? = null,
)

@Serializable
data class GeminiErrorPayload(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null,
)
