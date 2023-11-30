package com.example.mutsu.loginRegistration

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Users(var email: String)

interface AuthRepository {
    fun currentUser() : StateFlow<Users?>

    suspend fun signUp(email: String, password: String): Boolean

    suspend fun signIn(email: String, password: String): Boolean

    fun signOut()

    suspend fun delete()
}

