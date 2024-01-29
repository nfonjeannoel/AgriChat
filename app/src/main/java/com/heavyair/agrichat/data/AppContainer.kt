package com.heavyair.agrichat.data

import com.heavyair.agrichat.services.AccountServiceRepository
import com.heavyair.agrichat.services.FirebaseAccountService

interface AppContainer {
    val accountServiceRepository: AccountServiceRepository
}


class DefaultAppContainer : AppContainer {
    override val accountServiceRepository: AccountServiceRepository =
        FirebaseAccountService()
}