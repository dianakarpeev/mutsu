package com.example.mutsu

data class Ingredient(val name: String, var portions : Int)
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
