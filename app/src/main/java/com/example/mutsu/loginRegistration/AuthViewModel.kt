package com.example.mutsu.loginRegistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*
 * The code and logic in this file was copied/modified from Talib's notes and
 * github repository code found here:
 * https://github.com/tshussain/KotlinWithCompose
 */

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun currentUser(): StateFlow<Users?> {
        return authRepository.currentUser()
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signUp(email, password)
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.signIn(email, password)
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.delete()
        }
    }
}