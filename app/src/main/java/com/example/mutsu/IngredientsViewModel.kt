package com.example.mutsu

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emptyactivity.repositories.IngredientsNameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IngredientsViewModel(dataStore : DataStore<IngredientsName>, context : Context) : ViewModel(){
    private val ingredientsName = IngredientsNameRepository(dataStore, context)

    private val _ingredients = MutableStateFlow<List<FoodItem>>(emptyList())

    val ingredients: StateFlow<List<FoodItem>> = _ingredients.asStateFlow()
    var _editableList : List<FoodItem> = emptyList()

    init {
        viewModelScope.launch {

            var mapOf : Map<String, String> = ingredientsName.ingredientsNameFlow.first()

            if (mapOf.isEmpty()){
                ingredientsName.seedMap(mapOf)
                mapOf = ingredientsName.ingredientsNameFlow.first()
            }

            initializeIngredients(mapOf)
        }

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

     fun initializeIngredients(map : Map<String, String>) : Unit {
        var foodList = mutableListOf<FoodItem>()
        viewModelScope.launch {
            for (i in map){
                foodList.add(FoodItem(i.value, 0))
            }

            _editableList = foodList
            _ingredients.update { ings -> copyList() }
        }
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