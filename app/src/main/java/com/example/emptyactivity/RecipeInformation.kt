package com.example.emptyactivity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

@Preview
@Composable
fun RecipeInformation(modifier: Modifier = Modifier){
    val hardcodedIngredients = mutableListOf(
        TemporaryIngredient(2, Measurements.CUP, "Sugar"),
        TemporaryIngredient(500, Measurements.GRAM, "Flour"),
        TemporaryIngredient(1, Measurements.TABLESPOON, "Salt")
    )

    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        ButtonRow()
        RecipeForm(existingIngredients = hardcodedIngredients)
    }
}

@Composable
fun RecipeForm(
    modifier: Modifier = Modifier,
    existingIngredients: MutableList<TemporaryIngredient>
) {
    var recipeName by remember { mutableStateOf(TextFieldValue()) }
    var webURL by remember { mutableStateOf(TextFieldValue()) }

    val topSpacerHeight = 16
    val inBetweenSpacerHeight = 5

    Spacer(modifier = Modifier.height(topSpacerHeight.dp))
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Recipe Name
        UserInput(
            "Name",
            recipeName,
            onValueChange = { recipeName = it }
        )
        Spacer(modifier = Modifier.height(inBetweenSpacerHeight.dp))

        //Ingredients
        Text(text = "Ingredients")
        IngredientDisplay(existingIngredients = existingIngredients)
        Spacer(modifier = Modifier.height(inBetweenSpacerHeight.dp))

        //Web URL
        UserInput(
            "Web URL",
            webURL,
            onValueChange = { webURL = it }
        )
        Spacer(modifier = Modifier.height(inBetweenSpacerHeight.dp))
    }
}

@Composable
fun UserInput(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth()
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
        Button(
            onClick = { },
            modifier = modifier.padding(0.dp, 5.dp)
        ) {
            Text(text = "Delete")
        }
    }
}

@Composable
fun IngredientDisplay( existingIngredients: MutableList<TemporaryIngredient>) {
    val ingredientRows = generateIngredientRows(existingIngredients)
    var displayInputRow by remember { mutableStateOf(false) }

    LazyColumn {
        items(ingredientRows.size) { index ->
            ingredientRows[index]
        }

        item{
            AddIngredientButton(onClick = { displayInputRow = true})
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
    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Ingredient")
        }
    }
}

@Composable
private fun generateIngredientRows(ingredients: List<TemporaryIngredient>): List<@Composable () -> Unit> {
    return ingredients.map { ingredient ->
        { IngredientDisplayRow(ingredient = ingredient) }
    }
}

@Composable
fun IngredientDisplayRow(
    ingredient: TemporaryIngredient
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(ingredient.quantity.toString())
        Text(ingredient.measurement.abbreviation)
        Text(ingredient.name)
    }
}

@Composable
fun IngredientInputRow(existingIngredients : MutableList<TemporaryIngredient>) {
    var ingredientQuantity by remember { mutableStateOf("") }
    var ingredientMeasurement by remember { mutableStateOf(Measurements.NONE) }
    var ingredientName by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        //Quantity
        UserInput(
            label = "Quantity",
            value = TextFieldValue(ingredientQuantity),
            onValueChange = { ingredientQuantity = it.toString() }
        )

        //Measurement
        DropdownMeasurement(
            onMeasurementSelected = {
                selectedMeasurement -> ingredientMeasurement = selectedMeasurement
            }
        )

        //Name
        UserInput(
            label = "Name",
            value = TextFieldValue(ingredientName),
            onValueChange = { ingredientName = it.toString() })

        //Submit
        PlusButton( onClick = {
            existingIngredients.add(
                TemporaryIngredient(
                    ingredientQuantity.toInt(),
                    ingredientMeasurement,
                    ingredientName
                )
            )
        } )
    }
}

@Composable
fun PlusButton(
    onClick: () -> Unit,
    size : Int = 15
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

@Composable
fun DropdownMeasurement(onMeasurementSelected: (Measurements) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedMeasurement by remember { mutableStateOf(Measurements.NONE) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        Measurements.values().forEach { measurement ->
            DropdownMenuItem(
                { Text(text = measurement.abbreviation) },
                onClick = {
                    selectedMeasurement = measurement
                    onMeasurementSelected(measurement)
                    expanded = false
                }
            )
        }
    }
}