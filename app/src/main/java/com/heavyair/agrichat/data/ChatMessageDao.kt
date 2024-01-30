package com.heavyair.agrichat.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages_entity WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getChatMessages(sessionId: String): Flow<List<ChatMessageEntity>>


    @Query("SELECT * FROM chat_messages_entity WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getChatMessagesSuspend(sessionId: String): List<ChatMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(chatMessage: ChatMessageEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateChatMessage(chatMessage: ChatMessageEntity)

    @Query("DELETE FROM chat_messages_entity WHERE sessionId = :sessionId")
    suspend fun deleteChatMessages(sessionId: String)


    @Query("SELECT * FROM chat_messages_entity ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMessage(): ChatMessageEntity


    // get session history to display in history screen. Return a list of SessionId and the first message of each session
    @Query("SELECT sessionId, content FROM chat_messages_entity GROUP BY sessionId ORDER BY timestamp DESC LIMIT 20")
    fun getSessionHistory(): Flow<List<SessionHistory>>
}