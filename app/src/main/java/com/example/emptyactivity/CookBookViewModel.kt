package com.example.emptyactivity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CookbookViewModel : ViewModel() {
    private val _recipeList = mutableStateListOf<Recipe>()
    private val _recipeName = mutableStateOf("")

    val recipeName: State<String> = _recipeName
    val recipeList = _recipeList

    fun addRecipe(recipe: Recipe) {
        _recipeList.add(recipe)
        _recipeName.value = ""
    }
}