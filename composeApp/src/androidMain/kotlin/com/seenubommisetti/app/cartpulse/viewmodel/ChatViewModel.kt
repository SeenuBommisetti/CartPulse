package com.seenubommisetti.app.cartpulse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.seenubommisetti.app.cartpulse.data.DefaultShoppingAssistantRepository
import com.seenubommisetti.app.cartpulse.domain.SendShoppingQueryUseCase
import com.seenubommisetti.app.cartpulse.models.Message
import com.seenubommisetti.app.cartpulse.network.GeminiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<Message> = listOf(
        Message(
            text = "Tell me what you're shopping for, your budget, and any must-have features.",
            isUser = false,
        )
    ),
    val input: String = "",
    val isLoading: Boolean = false,
)

class ChatViewModel(
    private val sendShoppingQuery: SendShoppingQueryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputChanged(value: String) {
        _uiState.update { it.copy(input = value) }
    }

    fun sendMessage() {
        val query = uiState.value.input.trim()
        if (query.isEmpty() || uiState.value.isLoading) return

        val userMessage = Message(text = query, isUser = true)
        val updatedConversation = uiState.value.messages + userMessage

        _uiState.update {
            it.copy(
                input = "",
                messages = updatedConversation,
                isLoading = true,
            )
        }

        viewModelScope.launch {
            val assistantMessage = runCatching { sendShoppingQuery(updatedConversation) }
                .getOrElse { throwable ->
                    Message(
                        text = throwable.message ?: "Something went wrong while generating recommendations.",
                        isUser = false,
                    )
                }

            _uiState.update { state ->
                state.copy(
                    messages = state.messages + assistantMessage,
                    isLoading = false,
                )
            }
        }
    }
}

class ChatViewModelFactory(
    private val apiKey: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(ChatViewModel::class.java))

        val repository = DefaultShoppingAssistantRepository(
            aiClient = GeminiClient(apiKey = apiKey),
        )

        @Suppress("UNCHECKED_CAST")
        return ChatViewModel(
            sendShoppingQuery = SendShoppingQueryUseCase(repository),
        ) as T
    }
}
