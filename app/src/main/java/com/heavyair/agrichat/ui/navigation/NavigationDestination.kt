package com.heavyair.agrichat.ui.navigation

import com.heavyair.agrichat.R

interface NavigationDestination {
    val route: String
    val titleRes: Int
}


object LoginDestination : NavigationDestination {
    override val route: String = "login"
    override val titleRes: Int = R.string.login
}


object SignUpDestination : NavigationDestination {
    override val route: String = "sign_up"
    override val titleRes: Int = R.string.sign_up
}

object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.home
}

object ShoppingDestination : NavigationDestination {
    override val route: String = "shopping"
    override val titleRes: Int = R.string.shopping
}