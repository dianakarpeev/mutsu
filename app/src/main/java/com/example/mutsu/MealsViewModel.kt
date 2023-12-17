package com.example.mutsu

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mutsu.repositories.RecipeRepository
import com.example.mutsu.repositories.StoredMealPlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MealsViewModel(datastore: DataStore<StoredRecipes>, mealPlanStore: DataStore<StoredMealPlan>, context : Context) : ViewModel() {
    private val storedRecipes = RecipeRepository(datastore, context)
    private val storedMealPlan = StoredMealPlanRepository(mealPlanStore)
    private val recipesFlow = storedRecipes.dataFlow

    private val _meals = MutableStateFlow<List<Meals>>(emptyList())

    val meals: StateFlow<List<Meals>> = _meals.asStateFlow()
    var editableList = mutableListOf<Meals>()

    init {
        viewModelScope.launch {
            getRecipesFromStorage()
        }

        removeDuplicates()
        _meals.update { _ -> copyList() }
    }

    //removes any duplicate recipes that found their way into the list
    private fun removeDuplicates(){
        var list = mutableListOf<Meals>()

        for (meal in editableList) {
            if(!list.contains(meal)){
                list.add(meal)
            }
        }

        editableList = list
    }

    fun getRecipesFromStorage(){
        viewModelScope.launch {
            recipesFlow.collect() { storedList ->
                storedList.recipesList.forEach{ storedRecipe ->
                    val recipe = storedRecipes.parseRecipeData(storedRecipe)
                    if (recipe.ingredients.isNotEmpty()){
                        val meal = Meals(recipe, 0)
                        editableList.add(meal)
                    }
                }
            }
        }
    }

    fun addMealPlan(){
        val mealPlan = mutableMapOf<String, Int>()

        for (meal in editableList){
            mealPlan[meal.recipe.name] = meal.quantity
        }

        //TODO: Find a better feeling approach. This *works* but it's dangerous
        runBlocking {
            storedMealPlan.addMealPlan(mealPlan)
        }
    }

    fun increaseQuantity(index: Int){
        viewModelScope.launch {
            _meals.update { _ -> emptyList() }

            editableList[index].quantity++

            _meals.update { _ -> copyList() }
        }
    }

    fun decreaseQuantity(index: Int){
        viewModelScope.launch {
            _meals.update { _ -> emptyList() }

            editableList[index].quantity--

            _meals.update { _ -> copyList() }
        }
    }

    private fun copyList() : List<Meals> {
        var list = mutableListOf<Meals>()

        for (i in editableList){
            list.add(Meals(i.recipe, i.quantity))
        }

        return list
    }
}