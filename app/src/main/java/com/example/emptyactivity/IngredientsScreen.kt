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
    Column(modifier = modifier.fillMaxWidth(),){
        ingredients.forEachIndexed{ index, it ->
            IngredientButtonBox(
                ingredientName = it.name,
                ingredientQuantity = it.quantityInCart,
                isAdded = { increaseQuantity(index) },
                isRemoved = { if (it.quantityInCart != 0) decreaseQuantity(index) },
                modifier = modifier.fillMaxWidth())
        }

        var message by rememberSaveable { mutableStateOf<String>("")}
        Button(
            onClick = {
                message = "Item in you grocery list:"

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