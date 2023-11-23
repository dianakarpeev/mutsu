package com.example.emptyactivity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun IngredientsScreen(viewModel: IngredientsViewModel = viewModel(), modifier: Modifier = Modifier){
    val ingredients by viewModel.ingredients.collectAsStateWithLifecycle()

    val increase: (Int) -> Unit = {
        viewModel.increaseQuantity(it)
    }
    val decrease: (Int) -> Unit = {
        viewModel.decreaseQuantity(it)
    }

    /*
    val ingredients = listOf<FoodItem>(
        FoodItem("Flour", 0),
        FoodItem("Apples", 0),
        FoodItem("Sugar", 0),
        FoodItem("Rice", 0),
        FoodItem("Chicken", 0),
        FoodItem("Peppers", 0),
        FoodItem("Mayonnaise", 0),
        FoodItem("Cherries", 0),
        FoodItem("Salmon", 0),
        FoodItem("Salt", 0),
    )*/

    Column(modifier = modifier.verticalScroll(rememberScrollState())){
        Instructions(modifier)
        ShowAllIngredients(ingredients, increase, decrease, modifier)
    }
}

/* Data class for the ingredients. */
data class FoodItem (val name: String, var quantityInCart: Int)

/* Prints the instructions for the page at the top of the screen. */
@Composable
fun Instructions(modifier: Modifier = Modifier){
    Column(){
        Text(text = "Grocery List!", style = MaterialTheme.typography.titleLarge, modifier = modifier.padding(6.dp))
        Text(text = "Here is your grocery list. Feel free to add or remove any ingredients found below.", modifier = modifier.padding(6.dp))
    }
}

/* Sets up the saveable remember states for all ingredients as well as calls the function to set up
*  the buttons to add the ingredients to the inventory or grocery list and the buttons to see the lists
*  for each.
*/
@Composable
fun ShowAllIngredients(ingredients: List<FoodItem>, increaseQuantity: (Int) -> Unit, decreaseQuantity: (Int) -> Unit, modifier: Modifier = Modifier){
    /*
    var flour by rememberSaveable { mutableStateOf<Int>(0) }
    var apple by rememberSaveable { mutableStateOf<Int>(0) }
    var sugar by rememberSaveable { mutableStateOf<Int>(0) }
    var rice by rememberSaveable { mutableStateOf<Int>(0) }
    var chicken by rememberSaveable { mutableStateOf<Int>(0) }
    var pepper by rememberSaveable { mutableStateOf<Int>(0) }
    var mayonnaise by rememberSaveable { mutableStateOf<Int>(0) }
    var cherry by rememberSaveable { mutableStateOf<Int>(0) }
    var salmon by rememberSaveable { mutableStateOf<Int>(0) }
    var salt by rememberSaveable { mutableStateOf<Int>(0) }

    var statefulIngredients by rememberSaveable{ mutableStateOf(mutableListOf<Int>())}

    ingredients.forEach{
        statefulIngredients.add(it.quantityInCart)
    }
*/
    Column(modifier = modifier.fillMaxWidth(),){
        ingredients.forEachIndexed{ index, it ->
            IngredientButtonBox(
                ingredientName = it.name,
                ingredientQuantity = it.quantityInCart,
                isAdded = { increaseQuantity(index) },
                isRemoved = { if (it.quantityInCart != 0) decreaseQuantity(index) },
                modifier = modifier.fillMaxWidth())
        }
        /*
        //flour
        IngredientButtonBox(
            ingredientName = ingredients[0].name,
            ingredientQuantity = flour,
            isAdded = { flour++ },
            isRemoved = { if (flour > 0){flour--} },
            modifier.fillMaxWidth(),)
        //apple
        IngredientButtonBox(
            ingredientName = ingredients[1].name,
            ingredientQuantity = apple,
            isAdded = { apple++ },
            isRemoved = { if (apple > 0){apple--} },
            modifier.fillMaxWidth(),)
        //sugar
        IngredientButtonBox(
            ingredientName = ingredients[2].name,
            ingredientQuantity = sugar,
            isAdded = { sugar++ },
            isRemoved = { if (sugar > 0){sugar--} },
            modifier.fillMaxWidth(),)
        //rice
        IngredientButtonBox(
            ingredientName = ingredients[3].name,
            ingredientQuantity = rice,
            isAdded = { rice++ },
            isRemoved = { if (rice > 0){rice--} },
            modifier.fillMaxWidth(),)
        //chicken
        IngredientButtonBox(
            ingredientName = ingredients[4].name,
            ingredientQuantity = chicken,
            isAdded = { chicken++ },
            isRemoved = { if (chicken > 0){chicken--} },
            modifier.fillMaxWidth(),)
        //pepper
        IngredientButtonBox(
            ingredientName = ingredients[5].name,
            ingredientQuantity = pepper,
            isAdded = { pepper++ },
            isRemoved = { if (pepper > 0){pepper--} },
            modifier.fillMaxWidth(),)
        //mayonnaise
        IngredientButtonBox(
            ingredientName = ingredients[6].name,
            ingredientQuantity = mayonnaise,
            isAdded = { mayonnaise++ },
            isRemoved = { if (mayonnaise > 0){mayonnaise--} },
            modifier.fillMaxWidth(),)
        //cherry
        IngredientButtonBox(
            ingredientName = ingredients[7].name,
            ingredientQuantity = cherry,
            isAdded = { cherry++ },
            isRemoved = { if (cherry > 0){cherry--} },
            modifier.fillMaxWidth(),)
        //salmon
        IngredientButtonBox(
            ingredientName = ingredients[8].name,
            ingredientQuantity = salmon,
            isAdded = { salmon++ },
            isRemoved = { if (salmon > 0){salmon--} },
            modifier.fillMaxWidth(),)
        //salt
        IngredientButtonBox(
            ingredientName = ingredients[9].name,
            ingredientQuantity = salt,
            isAdded = { salt++ },
            isRemoved = { if (salt > 0){salt--} },
            modifier.fillMaxWidth(),)
        */
        var message by rememberSaveable { mutableStateOf<String>("")}
        Button(
            onClick = {
                message = ""

                /*
                ingredients[0].quantityInCart = flour
                ingredients[1].quantityInCart = apple
                ingredients[2].quantityInCart = sugar
                ingredients[3].quantityInCart = rice
                ingredients[4].quantityInCart = chicken
                ingredients[5].quantityInCart = pepper
                ingredients[6].quantityInCart = mayonnaise
                ingredients[7].quantityInCart = cherry
                ingredients[8].quantityInCart = salmon
                ingredients[9].quantityInCart = salt
*/
                message += "Item in you grocery list:"

                for(i in ingredients.indices){
                    if (ingredients[i].quantityInCart > 0){
                        var name = ingredients[i].name
                        var quantity = ingredients[i].quantityInCart
                        message += "\n  - x$quantity $name"
                    }
                }
            },
            modifier = modifier.padding(6.dp)
        ){
            Text(text = "Generate final grocery list")
        }
        Text(text = "$message")
    }
}

/* Actually sets up the ingredient name and button and all the logic to add them to either list. */
@Composable
fun IngredientButtonBox(ingredientName: String, ingredientQuantity: Int, isAdded: () -> Unit, isRemoved: () -> Unit, modifier: Modifier = Modifier){
    Row(modifier = modifier
        .padding(6.dp)
        .background(MaterialTheme.colorScheme.tertiaryContainer)
        .border(
            BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
            shape = RectangleShape
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
    {
        Column(){
            Text(" $ingredientName: $ingredientQuantity", color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
        Row(){
            Button(onClick = isAdded, modifier = Modifier.padding(0.dp,0.dp,6.dp,0.dp)) {
                Text("+", color = MaterialTheme.colorScheme.onTertiary)
            }
            Button(onClick = isRemoved, modifier = Modifier.padding(0.dp,0.dp,6.dp,0.dp)) {
                Text("-", color = MaterialTheme.colorScheme.onTertiary)
            }
        }
    }
}