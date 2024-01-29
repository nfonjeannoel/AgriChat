package com.heavyair.agrichat.ui.screens.home

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heavyair.agrichat.R
import com.heavyair.agrichat.ui.navigation.HomeDestination
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heavyair.agrichat.AppViewModelProvider
import com.heavyair.agrichat.data.ChatMessageEntity
import com.heavyair.agrichat.data.ChatMessageType

@Composable
fun DrawerContent() {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Add more items or content here
    }
}


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var sessionId = viewModel.getSessionId()
    if (sessionId == null) {
        viewModel.startNewSession()
        sessionId = viewModel.getSessionId()!!
    }
    val chatUiState by viewModel.chatUiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val chatHistory by viewModel.getChatHistory(sessionId).collectAsState(initial = emptyList())


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet { DrawerContent() }
        },
    ) {
//        val scrollState = rememberScrollState()
        val context = LocalContext.current
        Scaffold(
            topBar = {
                HomeTopAppBar(
                    titleStrRes = R.string.chat,
                    onNavDrawerClicked = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .background(MaterialTheme.colorScheme.primary)
                    .clip(
                        RoundedCornerShape(
                            topStart = 22.dp,
                            topEnd = 22.dp,
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {

//                    Divider()
                    Column(
                        Modifier.weight(1f, fill = true),

                        ) {
                        MessagesSection(
                            chatUiState = chatUiState,
                            chatHistory = chatHistory
                        )

                    }

                    // buttom promt input section with send button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        OutlinedTextField(
                            value = chatUiState.userInput,
                            onValueChange = {
                                viewModel.onUserInputChanged(it)
                            },
                            modifier = Modifier
                                .weight(1f, fill = true)
                                .padding(0.dp)
//                                .defaultMinSize(minHeight = 28.dp)
                                .padding(start = 16.dp, end = 16.dp),
                            shape = MaterialTheme.shapes.large,
                            maxLines = 2,
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.type_a_message),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            trailingIcon = {
                                if (chatUiState.userInput.trim().isEmpty()) {
                                    IconButton(onClick = {
                                        Toast.makeText(
                                            context,
                                            "Coming soon",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.AttachFile,
                                            contentDescription = "attachment",
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .size(32.dp)
                                        )
                                    }
                                } else {
                                    IconButton(onClick = {
                                        viewModel.getCompletion(
                                            sessionId
                                        )
                                        viewModel.onUserInputChanged("")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowCircleUp,
                                            contentDescription = "menu",
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .size(32.dp)
                                        )
                                    }
                                }
                            }


                        )


                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MessagesSection(chatUiState: ChatUiState, chatHistory: List<ChatMessageEntity>) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
        ),
        exit = fadeOut(),
    ) {
        LazyColumn {
            itemsIndexed(chatHistory) { index, chatMessage ->
                MessageCard(
                    chatUiState = chatUiState,
                    chatMessage = chatMessage,
                    shouldStream = index == chatHistory.size - 1,
                    modifier = Modifier
                        // Animate each list item to slide in vertically
                        .animateEnterExit(
                            enter = slideInVertically(
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessVeryLow,
                                    dampingRatio = Spring.DampingRatioLowBouncy
                                ),
                                initialOffsetY = { it * (index + 1) } // staggered entrance
                            )
                        )
                )
            }
        }

    }
}

@Composable
fun MessageCard(
    chatUiState: ChatUiState,
    chatMessage: ChatMessageEntity,
    shouldStream: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(0.dp),
        border = null,
        modifier = modifier
//            .padding(8.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(
                    id = when (chatMessage.type) {
                        ChatMessageType.SENT -> R.drawable.person
                        ChatMessageType.RECEIVED -> R.drawable.agribot
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Top)
            )

            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = when (chatMessage.type) {
                        ChatMessageType.SENT -> "You"
                        ChatMessageType.RECEIVED -> "AgriChat"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                )
                Divider()
                if (chatUiState.loading && shouldStream) {
                    Text(text = chatUiState.message)
                } else {
                    Text(text = chatMessage.content)
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    onNavDrawerClicked: () -> Unit,
    @StringRes titleStrRes: Int = HomeDestination.titleRes,
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = titleStrRes),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavDrawerClicked) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "menu",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
    )
}