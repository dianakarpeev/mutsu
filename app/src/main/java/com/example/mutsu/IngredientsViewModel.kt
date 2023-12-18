package com.example.mutsu

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mutsu.repositories.IngredientsNameRepository
import com.example.mutsu.repositories.RecipeRepository
import com.example.mutsu.repositories.StoredMealPlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IngredientsViewModel(dataStore : DataStore<IngredientsName>, mealPlanStore: DataStore<StoredMealPlan>, recipeStore: DataStore<StoredRecipes>, context : Context) : ViewModel(){
    private val ingredientsName = IngredientsNameRepository(dataStore, context)
    private val storedMealPlan = StoredMealPlanRepository(mealPlanStore)
    private val storedRecipes = RecipeRepository(recipeStore, context)

    private val _ingredients = MutableStateFlow<List<FoodItem>>(emptyList())

    val ingredients: StateFlow<List<FoodItem>> = _ingredients.asStateFlow()
    var _editableList : List<FoodItem> = emptyList()


    init {
        viewModelScope.launch {

            parseIngredientsFromRecipes()
            var mapOf : Map<String, String> = ingredientsName.ingredientsNameFlow.first()

            if (mapOf.isEmpty()){
                ingredientsName.seedMap(mapOf)
                mapOf = ingredientsName.ingredientsNameFlow.first()
            }
            initializeIngredients(mapOf)
        }
        _ingredients.update { ings -> copyList() }
    }

    private fun parseIngredientsFromRecipes(){
        val list = mutableListOf<String>()

        viewModelScope.launch {
            storedRecipes.dataFlow.collect { storedList ->
                storedList.recipesList.forEach{ storedRecipe ->
                    val recipe = storedRecipes.parseRecipeData(storedRecipe)
                    if (recipe.ingredients.isNotEmpty()){
                        recipe.ingredients.forEach { ingredient ->
                            list.add(ingredient.name)
                        }
                    }
                }
                ingredientsName.parseIngredients(list)
            }
        }
    }

     fun initializeIngredients(map : Map<String, String>) : Unit {
        var foodList = mutableListOf<FoodItem>()
        viewModelScope.launch {
            for (i in map){
                foodList.add(FoodItem(foodList.size + 1,  i.value, 0, " "))
            }

            _editableList = foodList
            addMealPlan(_editableList)
            _ingredients.update { ings -> copyList() }
        }
    }

    suspend fun addMealPlan(list : List<FoodItem>) {
        viewModelScope.launch {
            val mealPlan = storedMealPlan.getMealPlanAsMap()
            //For each recipe in the list, create a map of ingredients and their quantities
            for (meal in mealPlan) {
                val recipeName = meal.key
                val recipe = storedRecipes.getRecipeByName(recipeName)

                if (recipe != null) {
                    //Parse stored recipe into a recipe object
                    val recipe = storedRecipes.parseRecipeData(recipe)
                    //For each ingredient by name, add the quantity to corresponding ingredient in the list
                    for (ingredient in recipe.ingredients) {
                        val index = list.indexOfFirst { it.name == ingredient.name }
                        if (index != -1) {
                            var ingredientCount = meal.value * ingredient.quantity
                            var measurement = ingredient.measurement.abbreviation
                            list[index].quantityInCart = (list[index].quantityInCart + ingredientCount).toInt()
                            list[index].measurement = measurement
                        }
                    }
                }
            }
        }
        _ingredients.update { ings -> copyList() }
    }

    fun increaseQuantity(id: Int){
        viewModelScope.launch {
            _ingredients.update { ings -> emptyList() }

            val index = _editableList.indexOfFirst { it.id == id }
            _editableList[index].quantityInCart++

            _ingredients.update { ings -> copyList() }
        }
    }

    fun decreaseQuantity(id: Int){
        viewModelScope.launch {
            _ingredients.update { ings -> emptyList() }

            val index = _editableList.indexOfFirst { it.id == id }
            _editableList[index].quantityInCart--

            _ingredients.update { ings -> copyList() }
        }
    }

    private fun copyList(): List<FoodItem>{
        var list = mutableListOf<FoodItem>()

        for (i in _editableList){
            list.add(FoodItem(i.id, i.name, i.quantityInCart, i.measurement))
        }

        return list
    }
}