package com.example.mutsu

import android.app.Application
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/*
 * The code and logic in this file was copied/modified from Talib's notes and
 * github repository code found here:
 * https://github.com/tshussain/KotlinWithCompose
 */

class MyApp: Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModule(this, Firebase.auth)
    }
}