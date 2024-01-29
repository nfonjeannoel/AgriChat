package com.heavyair.agrichat.data

import android.content.Context
import com.heavyair.agrichat.services.AccountServiceRepository
import com.heavyair.agrichat.services.FirebaseAccountService

interface AppContainer {
    val accountServiceRepository: AccountServiceRepository
    val chatMessageRepository: ChatMessageRepository
}


class DefaultAppContainer(
    val context: Context
) : AppContainer {
    override val accountServiceRepository: AccountServiceRepository =
        FirebaseAccountService()

    override val chatMessageRepository: ChatMessageRepository
        get() = OfflineMessageChatRepository(
            ChatMessageDatabase.getDatabase(context).chatMessageDao()
        )
}