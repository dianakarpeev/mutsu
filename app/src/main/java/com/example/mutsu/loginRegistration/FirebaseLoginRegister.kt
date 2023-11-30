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

class AuthRepositoryFirebase(private val auth: FirebaseAuth) : AuthRepository {
    private val currentUserStateFlow = MutableStateFlow(auth.currentUser?.toUser())

    init {
        auth.addAuthStateListener { firebaseAuth ->
            currentUserStateFlow.value = firebaseAuth.currentUser?.toUser()
        }
    }

    override fun currentUser(): StateFlow<Users?> {
        return currentUserStateFlow
    }

    private fun FirebaseUser?.toUser(): Users? {
        return this?.let {
            if (it.email==null) null else
                Users(
                    email = it.email!!,
                )
        }
    }
}