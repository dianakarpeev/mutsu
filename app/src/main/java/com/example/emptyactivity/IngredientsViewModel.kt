package com.example.emptyactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ingredientName{
    FLOUR,
    RICE,
    KETCHUP_CHIPS,
    WHITE_BREAD,
    HUMMUS,
    APPLE_JUICE,
    CHICKEN,
    APPLES,
    CUCUMBER,
    TAPIOCA,
    SALT
}

class IngredientsViewModel : ViewModel(){
    private val _ingredients = MutableStateFlow<List<FoodItem>>(emptyList())
    private val _editableList = instantiateIngredients()

    val ingredients: StateFlow<List<FoodItem>> = _ingredients.asStateFlow()

    init {
        _ingredients.value = instantiateIngredients()
    }

    private fun instantiateIngredients() : List<FoodItem>{
        var list = mutableListOf<FoodItem>()
        list.add(FoodItem("Flour", 0))
        list.add(FoodItem("Rice", 0))
        list.add(FoodItem("Ketchup Chips", 0))
        list.add(FoodItem("White Bread", 0))
        list.add(FoodItem("Hummus", 0))
        list.add(FoodItem("Apple Juice", 0))
        list.add(FoodItem("Chicken", 0))
        list.add(FoodItem("Apples", 0))
        list.add(FoodItem("Cucumber", 0))
        list.add(FoodItem("Tapioca", 0))
        list.add(FoodItem("Salt", 0))

        return list
    }

    fun increaseQuantity(index: Int){
        viewModelScope.launch {
            _ingredients.update { ings -> emptyList() }

            _editableList[index].quantityInCart++

            _ingredients.update { ings -> copyList() }
        }
    }

    fun decreaseQuantity(index: Int){
        viewModelScope.launch {
            _ingredients.update { ings -> emptyList() }

            _editableList[index].quantityInCart--

            _ingredients.update { ings -> copyList() }
        }
    }

    private fun copyList(): List<FoodItem>{
        var list = mutableListOf<FoodItem>()

        for (i in _editableList){
            list.add(FoodItem(i.name, i.quantityInCart))
        }

        return list
    }
}