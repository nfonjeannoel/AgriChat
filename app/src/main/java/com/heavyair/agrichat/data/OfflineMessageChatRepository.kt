package com.heavyair.agrichat.data

import kotlinx.coroutines.flow.Flow

class OfflineMessageChatRepository(
    private val chatMessageDao: ChatMessageDao
) : ChatMessageRepository {
    override fun getChatMessages(sessionId: String): Flow<List<ChatMessageEntity>> =
        chatMessageDao.getChatMessages(sessionId)

    override suspend fun insertChatMessage(chatMessage: ChatMessageEntity) =
        chatMessageDao.insertChatMessage(chatMessage)

    override suspend fun deleteChatMessages(sessionId: String) =
        chatMessageDao.deleteChatMessages(sessionId)
}