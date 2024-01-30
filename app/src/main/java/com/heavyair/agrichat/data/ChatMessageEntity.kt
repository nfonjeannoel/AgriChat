package com.heavyair.agrichat.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages_entity")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sessionId: String,
    var content: String,
    val type: ChatMessageType,
    val timestamp: Long = System.currentTimeMillis()
)


enum class ChatMessageType {
    SENT, RECEIVED
}


data class SessionHistory(
    val sessionId: String,
    val content: String
)