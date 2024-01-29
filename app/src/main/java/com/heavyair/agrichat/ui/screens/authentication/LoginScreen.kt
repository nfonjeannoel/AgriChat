package com.heavyair.agrichat.ui.screens.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heavyair.agrichat.AppViewModelProvider
import com.heavyair.agrichat.R

@Composable
fun LoginScreen(
    onCreateAccountClicked: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: AccountViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val accountUiState by viewModel.accountUiState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp), // Increase padding
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//        Text(
//            stringResource(id = R.string.login), style = MaterialTheme.typography.titleLarge,
//            fontWeight = FontWeight.Bold
//        ) // Add app name

        Spacer(modifier = Modifier.height(32.dp)) // Add some space

        if (accountUiState.errorMessage.isNotEmpty()) {
            Text(
                text = accountUiState.errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (accountUiState.loggedIn) {
            LaunchedEffect(key1 = accountUiState.loggedIn) {
                onNavigateToHome()
            }
        }

        OutlinedTextField( // Use OutlinedTextField
            value = accountUiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Email") },
            isError = emailError != null,
            singleLine = true,
            supportingText = {
                emailError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            // Use OutlinedTextField
            value = accountUiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            singleLine = true,
            isError = passwordError != null,
            supportingText = {
                passwordError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (accountUiState.loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp),
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            Button(
                onClick = {
                    if (viewModel.validateCredentials()) {
                        viewModel.loginUser(
                            accountUiState.email,
                            accountUiState.password
                        )
                    } else {

                    }
                },

                ) {
                Text("Login")
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onCreateAccountClicked) { // Add "Create Account" button
            Text("Create Account")
        }
    }
}

