package com.heavyair.agrichat.services

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FirebaseAccountService : AccountServiceRepository {

    override fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Throwable?) -> Unit
    ) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun createAccount(email: String, password: String, onResult: (Throwable?) -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { onResult(it.exception) }
    }
}
