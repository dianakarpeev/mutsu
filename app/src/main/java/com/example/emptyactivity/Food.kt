package com.example.emptyactivity

//import IngredientsName
import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import java.io.InputStream


data class Ingredient(val name: String, var portions : Int)


//IngredientsName Class to serialize with Proto Datastore
class IngredientName() {
    private val ingredientMap = mutableMapOf<String, String>()

    init {
        ingredientMap["BREAD"] = "Bread"
        ingredientMap["BEEF_PATTY"] = "Beef Patty"
        ingredientMap["TOMATO"] = "Tomato"
        ingredientMap["ONION"] = "Onion"
        ingredientMap["CABBAGE"] = "Cabbage"
        ingredientMap["BEEF"] = "Beef"
        ingredientMap["CARROT"] = "Carrot"
        ingredientMap["POTATO"] = "Potato"
        ingredientMap["BACON"] = "Bacon"
        ingredientMap["EGG"] = "Egg"
    }

    private fun doesIngredientExist(ingredientName: String): Boolean {
        return ingredientMap.containsKey(ingredientName)
    }

    //Given the value of an ingredient, return the key
    fun getIngredientKey(ingredientName: String): String {
        //Replace spaces with underscores and make all letters uppercase
        ingredientName.replace(" ", "_").uppercase()

        return ingredientMap[ingredientName]!!
    }

    //Given the key of an ingredient, return the value
    fun getIngredientName(ingredientKey: String): String {
        return ingredientMap[ingredientKey]!!
    }

    //If the ingredient doesn't exist, add it to the map
    fun addIngredient(ingredientName: String) {
        if (!doesIngredientExist(ingredientName)) {
            val key = ingredientName.replace(" ", "_").uppercase()
            ingredientMap[key] = ingredientName
        }
    }

    fun getAllValues(): MutableCollection<String> {
        return ingredientMap.values
    }

}

class IngredientsNameRepository(private val dataStore: DataStore<IngredientsName>, context: Context) {

}






val ingredients = IngredientName()
val testIngredient = Ingredient(ingredients.getIngredientName("BREAD"), 2)








data class Meal(val name: String, val ingredients: List<Ingredient>)


//Hamburger
val bread = Ingredient("Bread", 2)
val beefPatty = Ingredient("Beef Patty", 1)
val tomato = Ingredient("Tomato", 1)
val onion = Ingredient("Onion", 1)
val cabbage = Ingredient("Cabbage", 1)

//Beef Stew
val beef = Ingredient("1/4lb of Beef", 1)
val carrot = Ingredient("Carrot", 2)
val potato = Ingredient("Potato", 2)

//Bacon and Eggs
val bacon = Ingredient("Bacon", 4)
val eggs = Ingredient("Egg", 2)

val hamburgerIngredient = listOf(bread, beefPatty, tomato, onion, cabbage)
val beefStewIngredient = listOf(beef, carrot, potato)
val baconAndEggsIngredient = listOf(bread, bacon, eggs)


val hamburgerMeal = Meal("Hamburger", hamburgerIngredient)
val beefStewMeal = Meal("Beef Stew", beefStewIngredient)
val baconAndEggsMeal = Meal("Bacon and Eggs", baconAndEggsIngredient)


