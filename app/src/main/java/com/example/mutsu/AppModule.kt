package com.example.mutsu

import android.content.Context
import com.example.mutsu.loginRegistration.AuthRepository
import com.example.mutsu.loginRegistration.AuthRepositoryFirebase
import com.google.firebase.auth.FirebaseAuth

class AppModule(
    private val appContext: Context,
    private val auth: FirebaseAuth
) {
    val authRepository : AuthRepository by lazy {
        AuthRepositoryFirebase(auth)
    }
}