package com.heavyair.agrichat.ui.screens.common

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.heavyair.agrichat.R
import com.heavyair.agrichat.ui.navigation.HomeDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    @StringRes titleStrRes: Int,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    canNavigateBack: Boolean = false
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(id = titleStrRes))
        },
        modifier = modifier,
        // navigate back button
        navigationIcon = {
            if (canNavigateBack){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable { onNavigateUp() }
                )
            }

        }
    )
}