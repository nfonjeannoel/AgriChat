package com.heavyair.agrichat.ui.screens.home

import android.util.Log
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
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heavyair.agrichat.R
import com.heavyair.agrichat.ui.navigation.HomeDestination
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.heavyair.agrichat.AppViewModelProvider
import com.heavyair.agrichat.data.ChatMessageEntity
import com.heavyair.agrichat.data.ChatMessageType
import com.heavyair.agrichat.data.SessionHistory
import kotlinx.serialization.json.JsonNull.content

@Composable
fun DrawerContent(
    onNewChatClicked: () -> Unit,
    onHistorySessionClicked: (String) -> Unit,
    sessionHistory: List<SessionHistory>,
    onLogout: () -> Unit,
    currentUser: FirebaseUser?,
    onShoppingClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(16.dp)
            .sizeIn(maxWidth = 250.dp)

    ) {
        // App Logo
        Image(
            painter = painterResource(id = R.drawable.agrichat_load_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 80.dp)
                .wrapContentHeight(Alignment.CenterVertically)
                .align(Alignment.CenterHorizontally),

            contentScale = ContentScale.Fit
        )
        NavDrawerLine()

        NavDrawerItem(
            onItemClick = { onNewChatClicked() },
            menuName = "New Chat",
            navIcon = Icons.Outlined.Sms
        )
        NavDrawerItem(
            onItemClick = { onShoppingClicked() },
            menuName = "Purchase",
            navIcon = Icons.Outlined.ShoppingCart
        )
        NavDrawerItem(
            onItemClick = { },
            menuName = "Subscription",
            navIcon = Icons.Outlined.Subscriptions
        )
        NavDrawerLine()
        Text(
            text = "Chat History",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
        )
        LazyColumn(modifier = Modifier.weight(1f, fill = true)) {

            itemsIndexed(sessionHistory) { index, historySession ->
                TextButton(
                    onClick = { onHistorySessionClicked(historySession.sessionId) },
                ) {
//                    Text(text = "${it.content} ")
                    // crop it.content to about 10 words and add ... at the end. If smaller, add the it.sessionId
                    val content =
                        "${index + 1}. ${historySession.content} ${historySession.sessionId}"
                    Text(
                        text = content,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .widthIn(max = 200.dp)
                    )

                }
            }
        }

        NavDrawerLine()
        SuggestionRow()
        currentUser?.email?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall
            )
        }
        NavDrawerLine()
        NavDrawerItem(
            onItemClick = { onLogout() },
            menuName = "Logout",
            navIcon = Icons.Filled.Logout
        )


    }
}

@Composable
fun SuggestionRow() {
    val items = listOf(
        "Inorganic",
        "Organic",
        "Fertilizer",
        "Pesticide",
        "Herbicide",
        "Fungicide",
        "Insecticide",
        "Seed",
        "Fertilizer",
        "Pesticide",
        "Herbicide",
        "Fungicide",
        "Insecticide",
        "Seed"
    )


    LazyRow() {
        items(items = items) { item ->
            SuggestionChip(
                label = { Text(text = item) },
                onClick = {
                    Log.d("SuggestionRow", "onClick: $item")
                },
                modifier = Modifier
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun NavDrawerLine() {
    Divider(
        modifier = Modifier
            .widthIn(max = 200.dp)
    )
}

@Composable
fun NavDrawerItem(
    onItemClick: () -> Unit,
    menuName: String,
    navIcon: ImageVector
) {
    TextButton(onClick = { onItemClick() }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 4.dp)

        ) {
            Icon(
                imageVector = navIcon,
                contentDescription = menuName,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(26.dp)
            )
            Text(
                text = menuName,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}


@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    onShoppingClicked: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
//    var sessionId = viewModel.getSessionId()
//    if (sessionId == null) {
//        viewModel.startNewSession()
//        sessionId = viewModel.getSessionId()!!
//    }
    val sessionId by viewModel.sessionId.collectAsState()
    val chatUiState by viewModel.chatUiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val chatHistory by viewModel.getChatHistory(sessionId).collectAsState(initial = emptyList())
    val sessionHistory by viewModel.getSessionHistory().collectAsState(initial = emptyList())
    val currentUser = remember {
        Firebase.auth.currentUser
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    onNewChatClicked = {
                        viewModel.startNewSession()
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    sessionHistory = sessionHistory,
                    onHistorySessionClicked = { sessionId ->
                        viewModel.setSessionId(sessionId)
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    onLogout = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                        viewModel.logout()
                        onSignOut()
                    },
                    currentUser = currentUser,
                    onShoppingClicked = {
                        onShoppingClicked()

                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }

                    }
                )
            }
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
                            enabled = !chatUiState.loading,
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
//                                        viewModel.onUserInputChanged("")
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
                            enter = slideInVertically()
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(0.dp),
        border = null,
        modifier = modifier
//            .padding(8.dp)
            .fillMaxWidth()
            .displayCutoutPadding()
//            .clip(MaterialTheme.shapes.small)
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
//                Divider()
                Text(text = chatMessage.content)

                if (chatUiState.loading && shouldStream) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
//                        Image(
//                            painter = painterResource(
//                                id = R.drawable.agribot
//                            ),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .size(24.dp)
//                                .align(Alignment.Top)
//                        )

                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Top)
                        )

                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = "AgriChat",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(text = chatUiState.message)


                        }
                    }
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