package com.heavyair.agrichat.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.heavyair.agrichat.OpenAi.OpenAiPrompt
import com.heavyair.agrichat.PreferencesHelper
import com.heavyair.agrichat.data.ChatMessageEntity
import com.heavyair.agrichat.data.ChatMessageRepository
import com.heavyair.agrichat.data.ChatMessageType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import java.util.UUID

data class ChatUiState(
    val message: String = "",
    val userInput: String = "",
    val loading: Boolean = false,
    val error: String? = null
)


class HomeScreenViewModel(
    private val chatMessageRepository: ChatMessageRepository,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()

    fun getCompletion(sessionId: String) {
        addMessageToHistory(
            sessionId,
            _chatUiState.value.userInput.trim(),
            ChatMessageType.SENT
        )
//        val chatMessage = ChatMessageEntity(
//            sessionId = sessionId,
//            content = "",
//            type = ChatMessageType.RECEIVED
//        )
//        addMessageToHistory(chatMessage)
        val openAI =
            OpenAI("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") // Create an instance of OpenAI
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = OpenAiPrompt.systemPrompt
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = _chatUiState.value.userInput.trim()
                )
            )
        )

        viewModelScope.launch {
            _chatUiState.value = _chatUiState.value.copy(loading = true)
//            val lastMessage = chatMessageRepository.getLatestMessage()

            try {
                val completions: Flow<ChatCompletionChunk> =
                    openAI.chatCompletions(chatCompletionRequest)
                completions.collect { completionChunk ->
                    val response = completionChunk.choices[0].delta.content
                    response?.let {
                        _chatUiState.value = _chatUiState.value.copy(
                            message = _chatUiState.value.message + it,
                            loading = _chatUiState.value.loading,
                            userInput = _chatUiState.value.userInput,
                            error = _chatUiState.value.error
                        )
                    }
                }
                addMessageToHistory(
                    sessionId,
                    _chatUiState.value.message,
                    ChatMessageType.RECEIVED
                )

//                lastMessage.content = _chatUiState.value.message
//                updateChatMessage(lastMessage)


//                val response = completion.choices.first().message.content
//                response?.let {
//                    _chatUiState.value = _chatUiState.value.copy(message = it)
//                }
                _chatUiState.value = _chatUiState.value.copy(loading = false, error = "")
            } catch (e: Exception) {
                _chatUiState.value = _chatUiState.value.copy(error = e.message, loading = false)
//                deleteChatMessage(chatMessage)

            }
            _chatUiState.value = ChatUiState()

        }

    }

    private fun deleteChatMessage(chatMessage: ChatMessageEntity) {
        viewModelScope.launch {
            chatMessageRepository.deleteChatMessages(chatMessage.sessionId)
        }
    }


    private fun updateChatMessage(chatMessage: ChatMessageEntity) {
        viewModelScope.launch {
            chatMessageRepository.updateChatMessage(chatMessage)
        }
    }


    fun getChatHistory(sessionId: String) = chatMessageRepository.getChatMessages(sessionId)

    fun addMessageToHistory(sessionId: String, content: String, type: ChatMessageType) {
        viewModelScope.launch {
            val chatMessage = ChatMessageEntity(
                sessionId = sessionId,
                content = content,
                type = type
            )
            chatMessageRepository.insertChatMessage(chatMessage)
        }
    }


    fun addMessageToHistory(chatMessage: ChatMessageEntity) {
        viewModelScope.launch {
            chatMessageRepository.insertChatMessage(chatMessage)
        }
    }

    fun startNewSession() {
        val sessionId = UUID.randomUUID().toString()
        preferencesHelper.saveSessionId(sessionId)
    }

    fun getSessionId(): String? = preferencesHelper.getSessionId()

    fun onUserInputChanged(it: String) {
        _chatUiState.value = _chatUiState.value.copy(userInput = it)
    }


}