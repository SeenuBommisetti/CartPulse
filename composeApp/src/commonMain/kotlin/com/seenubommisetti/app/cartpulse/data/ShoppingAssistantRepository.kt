package com.seenubommisetti.app.cartpulse.data

import com.seenubommisetti.app.cartpulse.models.Message
import com.seenubommisetti.app.cartpulse.network.AiClient
import com.seenubommisetti.app.cartpulse.prompt.PromptBuilder

interface ShoppingAssistantRepository {
    suspend fun getAssistantReply(conversation: List<Message>): Message
}

class DefaultShoppingAssistantRepository(
    private val aiClient: AiClient,
    private val promptBuilder: PromptBuilder = PromptBuilder(),
) : ShoppingAssistantRepository {
    override suspend fun getAssistantReply(conversation: List<Message>): Message {
        val replyText = aiClient.generateReply(
            systemInstruction = promptBuilder.buildSystemInstruction(),
            conversation = promptBuilder.buildConversation(conversation),
        )

        return Message(
            text = replyText.trim(),
            isUser = false,
        )
    }
}
