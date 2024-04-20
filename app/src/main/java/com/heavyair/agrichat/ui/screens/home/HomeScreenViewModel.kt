package com.heavyair.agrichat.ui.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.heavyair.agrichat.OpenAi.OpenAiPrompt
import com.heavyair.agrichat.data.ChatMessageEntity
import com.heavyair.agrichat.data.ChatMessageRepository
import com.heavyair.agrichat.data.ChatMessageType
import com.heavyair.agrichat.data.SessionHistory
import com.heavyair.agrichat.services.AccountServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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
    private val accountServiceRepository: AccountServiceRepository
) : ViewModel() {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()

    private val _sessionId = MutableStateFlow("")
    val sessionId: StateFlow<String> = _sessionId.asStateFlow()

    //    private val _chatSessionHistory: MutableStateFlow<List<SessionHistory>> =
//        MutableStateFlow(listOf())
//    val chatSessionHistory: MutableStateFlow<List<SessionHistory>> = _chatSessionHistory
    val TEMPERATURE = 0.2
    val MAX_TOKENS = 300
    val FREQUENCY_PENALTY = 0.5
    val PRESENCE_PENALTY = 0.5

    init {
        startNewSession()
    }



    fun getCompletion(sessionId: String) {
        viewModelScope.launch {
            _chatUiState.value = _chatUiState.value.copy(
                loading = true,
                error = "",
                userInput = _chatUiState.value.userInput,
                message = "",

                )
            Log.d("ChatMessage", "getCompletion: ${_chatUiState.value.userInput}")
            val previousMessages = chatMessageRepository.getChatMessagesSuspend(sessionId)
            val chatMessageContext: MutableList<ChatMessage> = mutableListOf()

            var totalCharCount = 0

            previousMessages.forEach { chatMessageEntity ->
                totalCharCount += chatMessageEntity.content.length
                if (totalCharCount > 3000) {
                    return@forEach
                }
                chatMessageContext.add(
                    ChatMessage(
                        role = if (chatMessageEntity.type == ChatMessageType.SENT) ChatRole.User else ChatRole.Assistant,
                        content = chatMessageEntity.content
                    )
                )
            }

            Log.d("ChatMessage", chatMessageContext.toString())

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
                OpenAI("") // Create an instance of OpenAI

            val chatCompletionRequest = ChatCompletionRequest(
                model = ModelId("gpt-4"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.System,
                        content = OpenAiPrompt.systemPrompt
                    ),
                    *chatMessageContext.toTypedArray(),
                    ChatMessage(
                        role = ChatRole.User,
                        content = _chatUiState.value.userInput.trim()
                    )
                ),
                temperature = TEMPERATURE,
                maxTokens = MAX_TOKENS,
                topP = 1.0,
                frequencyPenalty = FREQUENCY_PENALTY,
                presencePenalty = PRESENCE_PENALTY,
            )

            Log.d("ChatMessage", chatCompletionRequest.messages.toString())


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

    fun getSessionHistory() = chatMessageRepository.getSessionHistory()

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
        _sessionId.value = UUID.randomUUID().toString()
    }

    fun onUserInputChanged(it: String) {
        _chatUiState.value = _chatUiState.value.copy(userInput = it)
    }

    fun setSessionId(sessionId: String) {
        _sessionId.value = sessionId
    }

    fun logout() {
        accountServiceRepository.signOut()
    }


}