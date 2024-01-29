package com.heavyair.agrichat.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

//import androidx.compose.material3.ModalDrawerSheet

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    drawerState: DrawerState = rememberDrawerState(initialValue = Closed),
    onChatClicked: (String) -> Unit,
    onNewChatClicked: () -> Unit,
    onIconClicked: () -> Unit = {},
    conversationViewModel: Any = "",
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    scope.launch {
//        conversationViewModel.initialize()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                AppDrawer(
                    onChatClicked = onChatClicked,
                    onNewChatClicked = onNewChatClicked,
                    onIconClicked = onIconClicked,
                )
            }
        },
        content = content
    )

}