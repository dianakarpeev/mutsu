package com.example.emptyactivity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

data class Recipe(
    var name: String,
    var ingredients: MutableList<TemporaryIngredient>,
    var portionYield: Int,
    var webURL: String?
)

enum class Measurements(val abbreviation: String) {
    TEASPOON("tsp"),
    TABLESPOON("tbsp"),
    CUP("cup"),
    POUND("lb"),
    GRAM("g"),
    KILOGRAM("kg"),
    OUNCE("oz"),
    MILLILITER("ml"),
    LITER("l"),
    NONE("")
}

data class TemporaryIngredient(
    val quantity: Int,
    val measurement: Measurements,
    val name: String
)

@Composable
fun RecipeInformation(
    navController: NavHostController
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val arguments = navBackStackEntry?.arguments
    val recipeName = arguments?.getString("recipeName") ?: ""

    val hardcodedIngredients = mutableListOf(
        TemporaryIngredient(6, Measurements.NONE, "Slices of bread"),
        TemporaryIngredient(2, Measurements.CUP, "Milk"),
        TemporaryIngredient(1, Measurements.CUP, "Sugar"),
        TemporaryIngredient(4, Measurements.NONE, "Eggs"),
        TemporaryIngredient(3, Measurements.TABLESPOON, "Butter"),
        TemporaryIngredient(1/2, Measurements.TEASPOON, "Vanilla extract"),
        TemporaryIngredient(1/4, Measurements.TEASPOON, "Cinnamon")
    )

    var recipe = initializeRecipe(
        recipeName = recipeName,
        existingRecipe = existingRecipe
    )

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(320.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            ButtonRow()
            RecipeForm(recipe = recipe)
        }
    }
}

fun initializeRecipe(
    recipeName : String,
    existingRecipe : Recipe?
) : Recipe {
    if (existingRecipe == null){
        return Recipe(
            name = recipeName,
            ingredients = mutableListOf(),
            portionYield = 0,
            webURL = null
        )
    } else
        return existingRecipe
}

@Composable
fun RecipeForm(
    modifier: Modifier = Modifier,
    recipe: Recipe
) {
    var displayInputRow by remember { mutableStateOf(false)}
    val toggleDisplayInputRow: () -> Unit = { displayInputRow = !displayInputRow }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Recipe Name
        UserFieldInput(
            "Name",
            TextFieldValue(recipe.name),
            onValueChange = { recipe.name = it.toString() }
        )

        //Ingredients
        IngredientDisplay(
            existingIngredients = recipe.ingredients,
            toggleDisplayInputRow = toggleDisplayInputRow,
            displayInputRow = displayInputRow
        )

        //Portion Yield
        UserFieldInput(
            "Portion yield",
            TextFieldValue(recipe.portionYield.toString()),
            onValueChange = { recipe.portionYield = it.toString().toInt()},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        //Web URL
        UserFieldInput(
            "Web URL",
            TextFieldValue(recipe.webURL!!),
            onValueChange = { recipe.webURL = it.toString() }
        )
    }
}

@Composable
fun UserFieldInput(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier : Modifier = Modifier.fillMaxWidth(),
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier,
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun ButtonRow(modifier: Modifier = Modifier){
    val spacerHeight = 8

    Spacer(modifier = Modifier.height(spacerHeight.dp))
    Row(modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ){
        Button(
            onClick = {},
            modifier = modifier.padding(0.dp, 5.dp)
        ) {
            Text(text = "Edit")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { },
            modifier = modifier.padding(0.dp, 5.dp)
        ) {
            Text(text = "Delete")
        }
    }
}

@Composable
fun IngredientDisplay(
    existingIngredients: MutableList<TemporaryIngredient>,
    toggleDisplayInputRow: () -> Unit,
    displayInputRow: Boolean
) {

    LazyColumn(modifier = Modifier.height(IntrinsicSize.Min)) {
        items(existingIngredients) { ingredient ->
            IngredientDisplayRow(ingredient = ingredient)
        }

        item{
            AddIngredientButton(onClick = toggleDisplayInputRow)
        }

        if (displayInputRow){
            item {
                IngredientInputRow(existingIngredients)
            }
        }
    }
}

@Composable
fun AddIngredientButton(onClick: () -> Unit) {
    Column {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick,
            shape = RectangleShape
        ) {
            Text("Add Ingredient")
        }
    }
}

@Composable
fun IngredientDisplayRow(
    ingredient: TemporaryIngredient
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(ingredient.quantity.toString())
        Spacer(modifier = Modifier.width(8.dp))
        Text(ingredient.measurement.abbreviation)
        Spacer(modifier = Modifier.width(16.dp))
        Text(ingredient.name)
    }
}

@Composable
fun IngredientInputRow(existingIngredients : MutableList<TemporaryIngredient>) {
    var ingredientQuantity by remember { mutableStateOf(TextFieldValue()) }
    var ingredientMeasurement by remember { mutableStateOf(Measurements.NONE) }
    var ingredientName by remember { mutableStateOf(TextFieldValue()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Quantity
        UserFieldInput(
            label = "Qty",
            value = ingredientQuantity,
            onValueChange = { ingredientQuantity = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.width(80.dp)
        )

        //Measurement
        DropdownMeasurement(
            modifier = Modifier.width(100.dp)
        )

        //Name
        UserFieldInput(
            label = "Name",
            value = ingredientName,
            onValueChange = { ingredientName = it },
            modifier = Modifier.width(150.dp)
        )

        //Submit
        PlusButton( onClick = {
            existingIngredients.add(
                TemporaryIngredient(
                    ingredientQuantity
                        .toString()
                        .toInt(),
                    ingredientMeasurement,
                    ingredientName.toString()
                )
            )
        } )
    }
}

@Composable
fun PlusButton(
    onClick: () -> Unit,
    size : Int = 60
) {
    val iconSize : Int = size - 5

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

//Taken from https://alexzh.com/jetpack-compose-dropdownmenu/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMeasurement(modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var selectedMeasurement by remember { mutableStateOf(Measurements.NONE) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {expanded = !expanded},
            modifier = modifier
        ) {
            TextField(
                value = selectedMeasurement.abbreviation,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Measurements.values().forEach { measurement ->
                    DropdownMenuItem(
                        { Text(text = measurement.abbreviation) },
                        onClick = {
                            selectedMeasurement = measurement
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}