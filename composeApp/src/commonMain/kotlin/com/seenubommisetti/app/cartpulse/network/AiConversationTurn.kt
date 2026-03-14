package com.seenubommisetti.app.cartpulse.network

data class AiConversationTurn(
    val role: Role,
    val text: String,
) {
    enum class Role {
        User,
        Assistant,
    }
}
