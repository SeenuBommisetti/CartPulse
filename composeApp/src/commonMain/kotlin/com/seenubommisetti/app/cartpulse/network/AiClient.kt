package com.seenubommisetti.app.cartpulse.network

interface AiClient {
    suspend fun generateReply(
        systemInstruction: String,
        conversation: List<AiConversationTurn>,
    ): String
}
