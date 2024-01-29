package com.heavyair.agrichat.ui.screens.home

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.heavyair.agrichat.OpenAi.OpenAiPrompt
import com.heavyair.agrichat.OpenAi.ResponseChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class ChatUiState(
    val message: String = "",
    val userInput: String = "",
    val loading: Boolean = false,
    val error: String? = null
)


class HomeScreenViewModel() : ViewModel() {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()

    fun getCompletion() {
        val openAI =
            OpenAI("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") // Create an instance of OpenAI
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = OpenAiPrompt.prompt
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = _chatUiState.value.message.trim()
                )
            )
        )

        viewModelScope.launch {
            _chatUiState.value = _chatUiState.value.copy(loading = true)
            try {
                val completions: Flow<ChatCompletionChunk> =
                    openAI.chatCompletions(chatCompletionRequest)
                completions.collect { completionChunk ->
                    val response = completionChunk.choices[0].delta.content
                    response?.let {
                        _chatUiState.value = _chatUiState.value.copy(
                            message = _chatUiState.value.message + it
                        )
                    }


                }

//                val response = completion.choices.first().message.content
//                response?.let {
//                    _chatUiState.value = _chatUiState.value.copy(message = it)
//                }
                _chatUiState.value = _chatUiState.value.copy(loading = false, error = "")
            } catch (e: Exception) {
                _chatUiState.value = _chatUiState.value.copy(error = e.message, loading = false)

            }

        }

    }

    fun onUserInputChanged(it: String) {
        _chatUiState.value = _chatUiState.value.copy(userInput = it)
    }

}