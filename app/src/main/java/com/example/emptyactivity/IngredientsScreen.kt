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

@Composable
fun IngredientsScreen(modifier: Modifier = Modifier){
    val ingredients = listOf<FoodItem>(
        FoodItem("Flour"),
        FoodItem("Apples"),
        FoodItem("Sugar"),
        FoodItem("Rice"),
        FoodItem("Chicken"),
        FoodItem("Peppers"),
        FoodItem("Mayonnaise"),
        FoodItem("Cherries"),
        FoodItem("Salmon"),
        FoodItem("Salt"),
    )

    Column(modifier = modifier.verticalScroll(rememberScrollState())){
        Instructions(modifier)
        ShowAllIngredients(ingredients, modifier)
    }
}

/* Data class for the ingredients. */
data class FoodItem (val name: String)

/* Prints the instructions for the app at the top of the screen. */
@Composable
fun Instructions(modifier: Modifier = Modifier){
    Column(){
        Text(text = "Ingredients! Let's keep track of them!", style = MaterialTheme.typography.titleLarge, modifier = modifier.padding(6.dp))
        Text(text = "More instructions about how it works here!", modifier = modifier.padding(6.dp))
    }
}

/* Sets up the saveable remember states for all ingredients as well as calls the function to set up
*  the buttons to add the ingredients to the inventory or grocery list and the buttons to see the lists
*  for each.
*/
@Composable
fun ShowAllIngredients(ingredients: List<FoodItem>, modifier: Modifier = Modifier){
    //variables for the ingredients you own
    var flourO by rememberSaveable { mutableStateOf<Int>(0) }
    var appleO by rememberSaveable { mutableStateOf<Int>(0) }
    var sugarO by rememberSaveable { mutableStateOf<Int>(0) }
    var riceO by rememberSaveable { mutableStateOf<Int>(0) }
    var chickenO by rememberSaveable { mutableStateOf<Int>(0) }
    var pepperO by rememberSaveable { mutableStateOf<Int>(0) }
    var mayonnaiseO by rememberSaveable { mutableStateOf<Int>(0) }
    var cherryO by rememberSaveable { mutableStateOf<Int>(0) }
    var salmonO by rememberSaveable { mutableStateOf<Int>(0) }
    var saltO by rememberSaveable { mutableStateOf<Int>(0) }

    //variables for the ingredients you want to get more of
    var flourW by rememberSaveable { mutableStateOf<Int>(0) }
    var appleW by rememberSaveable { mutableStateOf<Int>(0) }
    var sugarW by rememberSaveable { mutableStateOf<Int>(0) }
    var riceW by rememberSaveable { mutableStateOf<Int>(0) }
    var chickenW by rememberSaveable { mutableStateOf<Int>(0) }
    var pepperW by rememberSaveable { mutableStateOf<Int>(0) }
    var mayonnaiseW by rememberSaveable { mutableStateOf<Int>(0) }
    var cherryW by rememberSaveable { mutableStateOf<Int>(0) }
    var salmonW by rememberSaveable { mutableStateOf<Int>(0) }
    var saltW by rememberSaveable { mutableStateOf<Int>(0) }

    Row(){
        Column(modifier = modifier.fillMaxWidth(0.5F)){
            Text(text = "Ingredients you own:", style = MaterialTheme.typography.titleLarge, modifier = modifier.padding(6.dp))

            //flour
            IngredientButtonBox(ingredient = ingredients[0], isAdded = flourO, isClicked = { flourO++ }, modifier = modifier.fillMaxWidth())
            //apple
            IngredientButtonBox(ingredient = ingredients[1], isAdded = appleO, isClicked = { appleO++ }, modifier = modifier.fillMaxWidth())
            //sugar
            IngredientButtonBox(ingredient = ingredients[2], isAdded = sugarO, isClicked = { sugarO++ }, modifier = modifier.fillMaxWidth())
            //rice
            IngredientButtonBox(ingredient = ingredients[3], isAdded = riceO, isClicked = { riceO++ }, modifier = modifier.fillMaxWidth())
            //chicken
            IngredientButtonBox(ingredient = ingredients[4], isAdded = chickenO, isClicked = { chickenO++ }, modifier = modifier.fillMaxWidth())
            //pepper
            IngredientButtonBox(ingredient = ingredients[5], isAdded = pepperO, isClicked = { pepperO++ }, modifier = modifier.fillMaxWidth())
            //mayonnaise
            IngredientButtonBox(ingredient = ingredients[6], isAdded = mayonnaiseO, isClicked = { mayonnaiseO++ }, modifier = modifier.fillMaxWidth())
            //cherry
            IngredientButtonBox(ingredient = ingredients[7], isAdded = cherryO, isClicked = { cherryO++ }, modifier = modifier.fillMaxWidth())
            //salmon
            IngredientButtonBox(ingredient = ingredients[8], isAdded = salmonO, isClicked = { salmonO++ }, modifier = modifier.fillMaxWidth())
            //salt
            IngredientButtonBox(ingredient = ingredients[9], isAdded = saltO, isClicked = { saltO++ }, modifier = modifier.fillMaxWidth())

            AllSelectedIngredientsButton(selectedIngredients = listOf(flourO,appleO,sugarO,riceO,chickenO,pepperO,mayonnaiseO,cherryO,salmonO,saltO), ingredients = ingredients, message = "Update inventory", modifier = modifier)
        }
        Column(modifier = modifier){
            Text(text = "Ingredients to buy:", style = MaterialTheme.typography.titleLarge, modifier = modifier.padding(6.dp))

            //flour
            IngredientButtonBox(ingredient = ingredients[0], isAdded = flourW, isClicked = { flourW++ }, modifier = modifier.fillMaxWidth())
            //apple
            IngredientButtonBox(ingredient = ingredients[1], isAdded = appleW, isClicked = { appleW++ }, modifier = modifier.fillMaxWidth())
            //sugar
            IngredientButtonBox(ingredient = ingredients[2], isAdded = sugarW, isClicked = { sugarW++ }, modifier = modifier.fillMaxWidth())
            //rice
            IngredientButtonBox(ingredient = ingredients[3], isAdded = riceW, isClicked = { riceW++ }, modifier = modifier.fillMaxWidth())
            //chicken
            IngredientButtonBox(ingredient = ingredients[4], isAdded = chickenW, isClicked = { chickenW++ }, modifier = modifier.fillMaxWidth())
            //pepper
            IngredientButtonBox(ingredient = ingredients[5], isAdded = pepperW, isClicked = { pepperW++ }, modifier = modifier.fillMaxWidth())
            //mayonnaise
            IngredientButtonBox(ingredient = ingredients[6], isAdded = mayonnaiseW, isClicked = { mayonnaiseW++ }, modifier = modifier.fillMaxWidth())
            //cherry
            IngredientButtonBox(ingredient = ingredients[7], isAdded = cherryW, isClicked = { cherryW++ }, modifier = modifier.fillMaxWidth())
            //salmon
            IngredientButtonBox(ingredient = ingredients[8], isAdded = salmonW, isClicked = { salmonW++ }, modifier = modifier.fillMaxWidth())
            //salt
            IngredientButtonBox(ingredient = ingredients[9], isAdded = saltW, isClicked = { saltW++ }, modifier = modifier.fillMaxWidth())

            AllSelectedIngredientsButton(selectedIngredients = listOf(flourW,appleW,sugarW,riceW,chickenW,pepperW,mayonnaiseW,cherryW,salmonW,saltW), ingredients = ingredients, message = "Update inventory", modifier = modifier)
        }
    }
}

/* Actually sets up the ingredient name and button and all the logic to add them to either list. */
@Composable
fun IngredientButtonBox(ingredient: FoodItem, isAdded: Int, isClicked: () -> Unit, modifier: Modifier = Modifier){
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
        Text(" " + ingredient.name, color = MaterialTheme.colorScheme.onTertiaryContainer)
        Button(onClick = isClicked, modifier = Modifier.padding(0.dp,0.dp,6.dp,0.dp)) {
            if (isAdded % 2 == 0){
                Text("+", color = MaterialTheme.colorScheme.onTertiary)
            }
            else{
                Text("-", color = MaterialTheme.colorScheme.onTertiary)
            }
        }
    }
}

/* Gets all the selected ingredients from the inventory or grocery list and prints them at the bottom of the screen. */
@Composable
fun AllSelectedIngredientsButton(selectedIngredients: List<Int>, ingredients: List<FoodItem>, message: String, modifier: Modifier = Modifier){
    var ingredientsString by rememberSaveable { mutableStateOf<String>("")}

    Column(modifier.padding(6.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
        Button(onClick = {
            ingredientsString = ""
            for (i in 0..9){
                if (ingredientsString != ""){
                    ingredientsString += ", "
                }
                ingredientsString += ingredients[i].name
            }
        }){
            Text(text = "$message", color = MaterialTheme.colorScheme.onTertiary)
        }

        Text(text = "$ingredientsString", color = MaterialTheme.colorScheme.onTertiaryContainer)
    }
}






