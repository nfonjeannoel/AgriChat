package com.heavyair.agrichat

import android.app.Application
import com.heavyair.agrichat.data.AppContainer
import com.heavyair.agrichat.data.DefaultAppContainer

class AgriChatApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
