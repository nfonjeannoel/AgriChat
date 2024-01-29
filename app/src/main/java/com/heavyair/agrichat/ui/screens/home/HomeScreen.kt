package com.heavyair.agrichat.ui.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heavyair.agrichat.R
import com.heavyair.agrichat.ui.navigation.HomeDestination
import kotlinx.coroutines.launch

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
fun HomeScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet { DrawerContent() }
        },
    ) {
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
//            floatingActionButton = {
//                ExtendedFloatingActionButton(
//                    text = { Text("Show drawer") },
//                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
//                    onClick = {
//                        scope.launch {
//                            drawerState.apply {
//                                if (isClosed) open() else close()
//                            }
//                        }
//                    }
//                )
//            }
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

                    var promptText by remember {
                        mutableStateOf("")
                    }
//                    Divider()
                    Column (
                    ){
                        MessagesSection()

                    }
                    Spacer(
                        Modifier.weight(1f, fill = true),
                    )
                    // buttom promt input section with send button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        OutlinedTextField(
                            value = promptText, onValueChange = {
                                promptText = it
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
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowCircleUp,
                                        contentDescription = "menu",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .size(32.dp)
                                    )
                                }
                            }


                        )


                    }

                }
            }
        }
    }
}


@Composable
fun MessagesSection() {
    MessageCard(
        avatar = painterResource(id = R.drawable.ic_launcher_foreground),
        username = "You",
        message = "hi there"
    )
    MessageCard(
        avatar = painterResource(id = R.drawable.ic_launcher_foreground),
        username = "AgriChat",
        message = "Hello! How can I assist you today? Hello! How can I assist you today? Hello! How can I assist you today?"
    )
}

@Composable
fun MessageCard(
    avatar: Painter,
    username: String,
    message: String
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(0.dp),
        border = null,
        modifier = Modifier
            .padding(8.dp)
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
                painter = painterResource(id = R.drawable.person),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Top)
            )

            Column(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(text = "You")
                Divider()
                Text(text = "hi there sdfsdfsdkhfgsdkfjsd,hsdvf.dlsjf/lsdjfsdjfs")
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