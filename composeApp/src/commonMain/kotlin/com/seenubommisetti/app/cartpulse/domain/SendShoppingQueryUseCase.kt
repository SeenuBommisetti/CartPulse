package com.seenubommisetti.app.cartpulse.domain

import com.seenubommisetti.app.cartpulse.data.ShoppingAssistantRepository
import com.seenubommisetti.app.cartpulse.models.Message

class SendShoppingQueryUseCase(
    private val repository: ShoppingAssistantRepository,
) {
    suspend operator fun invoke(conversation: List<Message>): Message {
        require(conversation.any { it.isUser }) { "A user message is required before requesting a reply." }
        return repository.getAssistantReply(conversation)
    }
}
