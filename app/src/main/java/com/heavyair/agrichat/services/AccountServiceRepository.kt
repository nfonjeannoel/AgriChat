package com.heavyair.agrichat.services

interface AccountServiceRepository {
    fun createAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun signInWithEmailAndPassword(email: String, password: String, onResult: (Throwable?) -> Unit)

    // Logout
    fun signOut()
}