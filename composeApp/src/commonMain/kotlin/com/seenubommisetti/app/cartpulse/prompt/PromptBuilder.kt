package com.seenubommisetti.app.cartpulse.prompt

import com.seenubommisetti.app.cartpulse.models.Message
import com.seenubommisetti.app.cartpulse.network.AiConversationTurn

class PromptBuilder(
    private val systemPrompt: String = DEFAULT_SYSTEM_PROMPT,
) {
    fun buildSystemInstruction(): String = systemPrompt

    fun buildConversation(conversation: List<Message>): List<AiConversationTurn> {
        require(conversation.isNotEmpty()) { "Conversation must contain at least one message." }

        return conversation.map { message ->
            AiConversationTurn(
                role = if (message.isUser) {
                    AiConversationTurn.Role.User
                } else {
                    AiConversationTurn.Role.Assistant
                },
                text = message.text,
            )
        }
    }

    companion object {
        const val DEFAULT_SYSTEM_PROMPT =
            "You are an AI shopping assistant. Recommend 3-5 products with short explanations " +
                "and approximate price ranges. Focus on helping users make quick purchase decisions."
    }
}
