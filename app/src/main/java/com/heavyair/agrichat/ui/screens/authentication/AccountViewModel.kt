package com.heavyair.agrichat.ui.screens.authentication

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.heavyair.agrichat.services.AccountServiceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val accountRepository: AccountServiceRepository
) : ViewModel() {


    private val _accountUiState = MutableStateFlow(AccountUiState())
    val accountUiState: StateFlow<AccountUiState> = _accountUiState.asStateFlow()

    fun onEmailChange(it: String) {
        _accountUiState.value = _accountUiState.value.copy(email = it)
    }

    fun onPasswordChange(it: String) {
        _accountUiState.value = _accountUiState.value.copy(password = it)

    }

    fun setAsLoading() {
        _accountUiState.value = _accountUiState.value.copy(loading = true)
    }

    fun setAsNotLoading() {
        _accountUiState.value = _accountUiState.value.copy(loading = false)
    }

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            setAsLoading()
            accountRepository.createAccount(email, password) {
                if (it == null) {
                    _accountUiState.value =
                        _accountUiState.value.copy(loggedIn = true, errorMessage = "")
                } else {
                    _accountUiState.value = _accountUiState.value.copy(
                        loggedIn = false,
                        errorMessage = it.message.toString()
                    )
                    setAsNotLoading()
                }
            }

        }
    }

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    fun validateCredentials(): Boolean {
        val isEmailValid = accountUiState.value.email.isNotEmpty()
        _emailError.value = if (isEmailValid) null else "Email cannot be empty"

        // check that email is valid format
        val isEmailValidFormat =
            Patterns.EMAIL_ADDRESS.matcher(accountUiState.value.email).matches()
        _emailError.value = if (isEmailValidFormat) null else "Email is not valid format"

        val isPasswordValid = accountUiState.value.password.isNotEmpty()
        _passwordError.value = if (isPasswordValid) null else "Password cannot be empty"

        // password must be at least 5 characters
        val isPasswordValidLength = accountUiState.value.password.length >= 5
        _passwordError.value =
            if (isPasswordValidLength) null else "Password must be at least 5 characters"


        return isEmailValid && isPasswordValid && isPasswordValidLength && isEmailValidFormat
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            setAsLoading()
            accountRepository.signInWithEmailAndPassword(email, password) {
                if (it == null) {
                    val user = Firebase.auth.currentUser
                    if (user != null) {
                        // User information is available, you can use it as needed
                        // For example, you can access user.uid, user.email, etc.
                        _accountUiState.value =
                            _accountUiState.value.copy(loggedIn = true, errorMessage = "")
                    } else {
                        // Handle the case where user information is not available
                        _accountUiState.value = _accountUiState.value.copy(
                            loggedIn = false,
                            errorMessage = "User information is not available"
                        )
                        setAsNotLoading()

                    }
                } else {
                    _accountUiState.value = _accountUiState.value.copy(
                        loggedIn = false,
                        errorMessage = it.message.toString()
                    )
                    setAsNotLoading()

                }
            }
            delay(1000)
        }
    }


}

data class AccountUiState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val loggedIn: Boolean = false,
    val loading: Boolean = false
)