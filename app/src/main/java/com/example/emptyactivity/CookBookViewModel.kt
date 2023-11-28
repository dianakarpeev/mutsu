package com.example.emptyactivity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CookbookViewModel : ViewModel() {
    private val _recipeList = mutableStateListOf<Recipeee>()
    private val _recipeName = mutableStateOf("")

    val recipeName: State<String> = _recipeName
    val recipeList = _recipeList

    fun addRecipe(recipe: Recipeee) {
        _recipeList.add(recipe)
        _recipeName.value = ""
    }
}