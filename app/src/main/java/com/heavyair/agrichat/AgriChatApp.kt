package com.heavyair.agrichat

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.heavyair.agrichat.ui.navigation.AgriChatNavHost

@Composable
fun AgriChatApp(

) {
    AgriChatNavHost(
        currentUser = Firebase.auth.currentUser
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AgriChatApp()
}