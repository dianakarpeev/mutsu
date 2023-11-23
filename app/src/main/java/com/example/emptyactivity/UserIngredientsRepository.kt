package com.example.emptyactivity

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import java.util.prefs.Preferences

data class UserIngredients(
    val flour: Int,
    val rice: Int,
    val ketchupChips: Int,
    val whiteBread: Int,
    val hummus: Int,
    val appleJuice: Int,
    val chicken: Int,
    val apples: Int,
    val cucumber: Int,
    val tapioca: Int,
    val salt: Int
) {
    companion object {
        fun getDefaultInstance(): UserIngredients {
            return UserIngredients(
                flour = 0,
                rice = 0,
                ketchupChips = 0,
                whiteBread = 0,
                hummus = 0,
                appleJuice = 0,
                chicken = 0,
                apples = 0,
                cucumber = 0,
                tapioca = 0,
                salt = 0
            )
        }
    }
}

class UserIngredientsRepository (
    private val dataStore: DataStore<UserIngredients>) {

        val userIngredients: Flow<UserIngredients> = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(UserIngredients.getDefaultInstance())
                } else {
                    throw exception
                }
            }
    }