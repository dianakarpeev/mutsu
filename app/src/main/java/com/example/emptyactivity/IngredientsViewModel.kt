package com.example.emptyactivity

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class IngredientsViewModel : ViewModel(){
    private val _ingredients = MutableStateFlow<List<FoodItem>>(instantiateIngredients())

    val ingredients: StateFlow<List<FoodItem>> = _ingredients.asStateFlow()

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
        _ingredients.value[index].quantityInCart++
        //_ingredients.value.add(index, FoodItem("test", 12))
    }

    fun decreaseQuantity(index: Int){
        _ingredients.value[index].quantityInCart--
    }
}