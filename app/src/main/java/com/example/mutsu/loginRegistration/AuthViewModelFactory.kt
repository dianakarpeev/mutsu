package com.example.mutsu.loginRegistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mutsu.MyApp

/*
 * The code and logic in this file was copied/modified from Talib's notes and
 * github repository code found here:
 * https://github.com/tshussain/KotlinWithCompose
 */

class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(MyApp.appModule.authRepository) as T
    }
}