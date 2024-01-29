package com.heavyair.agrichat.data

import kotlinx.coroutines.flow.Flow

interface ChatMessageRepository {
    fun getChatMessages(sessionId: String): Flow<List<ChatMessageEntity>>
    suspend fun insertChatMessage(chatMessage: ChatMessageEntity)
    suspend fun deleteChatMessages(sessionId: String)

    suspend fun getLatestMessage(): ChatMessageEntity

    suspend fun updateChatMessage(chatMessage: ChatMessageEntity)
}