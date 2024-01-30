package com.heavyair.agrichat.data

import kotlinx.coroutines.flow.Flow

class OfflineMessageChatRepository(
    private val chatMessageDao: ChatMessageDao
) : ChatMessageRepository {
    override fun getChatMessages(sessionId: String): Flow<List<ChatMessageEntity>> =
        chatMessageDao.getChatMessages(sessionId)

    override suspend fun getChatMessagesSuspend(sessionId: String): List<ChatMessageEntity> {
        return chatMessageDao.getChatMessagesSuspend(sessionId)
    }

    override suspend fun insertChatMessage(chatMessage: ChatMessageEntity) =
        chatMessageDao.insertChatMessage(chatMessage)

    override suspend fun deleteChatMessages(sessionId: String) =
        chatMessageDao.deleteChatMessages(sessionId)

    override suspend fun getLatestMessage(): ChatMessageEntity {
        return chatMessageDao.getLatestMessage()
    }

    override suspend fun updateChatMessage(chatMessage: ChatMessageEntity) {
        return chatMessageDao.updateChatMessage(chatMessage)
    }

    override fun getSessionHistory(): Flow<List<SessionHistory>> {
        return chatMessageDao.getSessionHistory()
    }
}