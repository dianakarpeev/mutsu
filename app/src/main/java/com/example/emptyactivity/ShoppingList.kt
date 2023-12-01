package com.example.emptyactivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShoppingList(){
    Header()
}

@Composable
fun Header(){
    Row(){
        Text(
            text = "Plan your meals!",
            style = MaterialTheme.typography.titleMedium,
        )
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            painter = painterResource(id = R.drawable.receipt),
            contentDescription = null
        )
    }
}

@Composable
fun RecipeCard(
    recipeName: String,
    count: Int,
    increase: () -> Unit,
    decrease: () -> Unit
){
    Card {
        Column(){
            Text(recipeName)
            Row(){
                IconButton(
                    onClick = increase,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier.padding(
                        start = 0.dp,
                        top = 0.dp,
                        end = 6.dp,
                        bottom = 0.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add one $recipeName"
                    )
                }

                Text(text = count.toString())

                IconButton(
                    onClick = decrease,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier.padding(
                        start = 0.dp,
                        top = 0.dp,
                        end = 6.dp,
                        bottom = 0.dp
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.remove_icon),
                        contentDescription = "Remove one $recipeName"
                    )
                }
            }
        }
    }
}

@Composable
fun Hamburger(count: Int, increase: () -> Unit, decrease: () -> Unit) {
    val hamburgerImage = painterResource(id = R.drawable.hamburger)


    Row(modifier = Modifier
        .fillMaxWidth(),
    ) {
        Image(modifier = Modifier
            .size(48.dp)
            .clip(CircleShape),
            painter = hamburgerImage,
            contentDescription = "A pixelated hamburger.")
        Text(modifier = Modifier
            .padding(start = 8.dp, top = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            text = "HAMBURGER")
        Button(
            onClick = increase,) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Add Meal")
        }
        Button(
            onClick = decrease) {
            Icon(Icons.Filled.KeyboardArrowDown, "Subtract Meal")
        }
        Text(modifier = Modifier
            .padding(start = 8.dp, top = 12.dp),
            fontSize = 18.sp,
            text = "$count")
    }
}


@Composable
fun BeefStew(count: Int, increase: () -> Unit, decrease: () -> Unit) {
    val beefStewImage = painterResource(id = R.drawable.beefstew)


    Row(modifier = Modifier
        .fillMaxWidth(),
    ) {
        Image(modifier = Modifier
            .size(48.dp)
            .clip(CircleShape),
            painter = beefStewImage,
            contentDescription = "A pixelated image of beef stew.")
        Text(modifier = Modifier
            .padding(start = 8.dp, top = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            text = "BEEF STEW")
        Button(
            onClick = increase,) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Add Meal")
        }
        Button(onClick = decrease) {
            Icon(Icons.Filled.KeyboardArrowDown, "Subtract Meal")
        }
        Text(modifier = Modifier
            .padding(start = 8.dp, top = 12.dp),
            fontSize = 18.sp,
            text = "$count")
    }
}


@Composable
fun BaconAndEggs(count: Int, increase: () -> Unit, decrease: () -> Unit) {
    val baconAndEggsImage = painterResource(id = R.drawable.baconandeggs)

    Row(modifier = Modifier
        .fillMaxWidth(),
    ) {
        Image(modifier = Modifier
            .size(48.dp)
            .clip(CircleShape),
            painter = baconAndEggsImage,
            contentDescription = "A pixelated image of bacon and eggs.")
        Text(modifier = Modifier
            .padding(start = 8.dp, top = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            text = "BACON AND EGGS")
        Button(
            onClick = increase,) {
            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Add Meal")
        }
        Button(onClick = decrease) {
            Icon(Icons.Filled.KeyboardArrowDown, "Subtract Meal")
        }
        Text(modifier = Modifier
            .padding(start = 8.dp, top = 12.dp),
            fontSize = 18.sp,
            text = "$count")
    }
}

@Composable
fun foodCounter () {
    var hamburgerCount by rememberSaveable {mutableStateOf(0)}
    var beefStewCount by rememberSaveable {mutableStateOf(0)}
    var baconAndEggsCount by rememberSaveable {mutableStateOf(0)}

    val hamburgerDecrease = {if(hamburgerCount > 0) hamburgerCount--}
    val beefStewDecrease = {if(beefStewCount > 0) beefStewCount--}
    val baconAndEggsDecrease = {if(baconAndEggsCount> 0) baconAndEggsCount--}

    var allIngredients by rememberSaveable {mutableStateOf(mutableListOf<Ingredient>())}

    Column {
        Hamburger(hamburgerCount, increase = {hamburgerCount++}, decrease = hamburgerDecrease )
        BeefStew(beefStewCount, increase = {beefStewCount++}, decrease = beefStewDecrease)
        BaconAndEggs(baconAndEggsCount, increase = {baconAndEggsCount++}, decrease = baconAndEggsDecrease)
        Button(onClick = {allIngredients = generateList(hamburgerCount, beefStewCount, baconAndEggsCount,
            hamburgerDecrease, beefStewDecrease, baconAndEggsDecrease)}) {
            Text(text = "Generate Ingredients")
        }

        displayIngredients(allIngredients)

    }

}

fun generateList(hamburgers : Int, beefStews : Int, baconAndEggs : Int,
                 hamburgerDecrease : () -> Unit, beefstewDecrease : () -> Unit, baconAndEggsDecrease: () -> Unit): MutableList<Ingredient> {

    val meals = mutableListOf<Meal>()
    val ingredients = mutableListOf<Ingredient>()

    for(i in 1..hamburgers) {
        meals.add(hamburgerMeal)
        hamburgerDecrease()
    }
    for (i in 1..beefStews) {
        meals.add(beefStewMeal)
        beefstewDecrease()
    }
    for (i in 1..baconAndEggs) {
        meals.add(baconAndEggsMeal)
        baconAndEggsDecrease()
    }

    for((i, currentMeal) in meals.withIndex()) {
        val currentMealIngredients = currentMeal.ingredients
        for((j, currentIngredient) in currentMealIngredients.withIndex()) {
            ingredients.add(currentIngredient)
        }
    }

    return ingredients
}

@Composable
fun displayIngredients(ingredientList : List<Ingredient>) {

    //Mutable Map implementation aided by Github Copilot
    val ingredientMap = mutableMapOf<String, Int>()

    for((i, currentIngredient) in ingredientList.withIndex()) {
        if(ingredientMap.containsKey(currentIngredient.name)) {
            ingredientMap[currentIngredient.name] = ingredientMap[currentIngredient.name]!! + currentIngredient.portions
        }
        else {
            ingredientMap[currentIngredient.name] = currentIngredient.portions
        }
    }

    for((i, currentIngredient) in ingredientMap) {
        Text(text = "$i : $currentIngredient")
    }
}