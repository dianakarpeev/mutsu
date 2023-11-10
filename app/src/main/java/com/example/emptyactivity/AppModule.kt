package com.example.emptyactivity

import android.content.Context

class AppModule(
    private val appContext: Context) {
    val profileRepository : ProfileRepository by lazy {
        ProfileRepositoryDataStore(appContext)
    }
}
