package com.heavyair.agrichat.OpenAi

import com.aallam.openai.api.chat.ChatRole


data class ResponseChatMessage(
    val role: ChatRole,
    val content: String
)
