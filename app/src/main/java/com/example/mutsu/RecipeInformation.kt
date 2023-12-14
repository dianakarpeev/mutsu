package com.example.mutsu

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class Recipe(
    var name: String,
    var ingredients: MutableList<TemporaryIngredient>,
    var portionYield: Int,
    var webURL: String?
)

//All the measurements a user can pick out of when creating a new ingredient to add to a recipe
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
    val quantity: Double,
    val measurement: Measurements,
    val name: String
)

/**
 * Screen where users can:
 * - Fill out the missing information for a newly created recipe
 * - Edit the information of an already created recipe
 * - Delete a recipe
 *
 * @param recipeViewModel ViewModel that contains existing recipes and handles CRUD operations
 * @param recipeName Name of the recipe to obtain information about (to prefill the form fields)
 * @param goToRecipeList Navigation to the Recipe List screen. Used when the changes to the recipe
 * are saved or when the recipe is deleted.
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RecipeInformationScreen(
    recipeViewModel: RecipeViewModel,
    recipeName: String?,
    goToRecipeList: () -> Unit
){
    //The recipe name is passed through navigation arguments and may be null
    if (recipeName == null) throw IllegalStateException("Recipe name is missing.")


    val recipe = findRecipe(recipeName, recipeViewModel)

    //TODO: Replace temporary hardcoded values with responsive behavior
    //Temporary hardcoded values - to be modified when implementing responsive behavior
    val columnWidth = 350.dp
    val spaceBetweenElements = 10.dp

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(columnWidth)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spaceBetweenElements)
        ) {
            Spacer(Modifier.height(10.dp))
            //Save and delete buttons
            ButtonRow(
                recipeViewModel = recipeViewModel,
                recipe = recipe,
                originalRecipe = recipe,
                goToRecipeList = goToRecipeList
            )

            //Form containing all the information for users to fill/edit
            RecipeForm(recipe = recipe)
        }
    }
}

private fun findRecipe(recipeName: String, recipeView : RecipeViewModel): Recipe {
    val firstRecipes = recipeView.getAllRecipes()
    val secondRecipes = recipeView.recipeList.value
    return firstRecipes.find { it.name == recipeName } ?: createEmptyRecipe(recipeName)
}

private fun createEmptyRecipe(recipeName: String): Recipe{
    return Recipe(
        name = recipeName,
        ingredients = mutableListOf(),
        portionYield = 0,
        webURL = null
    )
}

@Composable
fun RecipeForm(
    modifier: Modifier = Modifier,
    recipe: Recipe
) {
    //Variables to toggle the new ingredient input row's visibility to users.
    var displayInputRow by remember { mutableStateOf(false)}
    val toggleDisplayInputRow: () -> Unit = { displayInputRow = !displayInputRow }

    //Fields other than ingredients for users to fill out.
    var nameState by remember { mutableStateOf(recipe.name) }
    var portionState by remember { mutableStateOf(recipe.portionYield.toString()) }
    var urlState by remember { mutableStateOf(recipe.webURL ?: "") }

    //Displays an error message under the corresponding fields when false.
    var isNameValid by remember { mutableStateOf(true) }
    var isPortionValid by remember { mutableStateOf(true) }

    //Error messages to display under corresponding fields when invalid.
    val invalidStringErrorMessage by remember { mutableStateOf("Please enter a valid string")}
    val invalidIntegerErrorMessage by remember { mutableStateOf("Please enter a valid positive number") }

    //Temporary hardcoded values - to be modified when implementing responsive behavior
    val spaceBetweenInputFields = 20.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spaceBetweenInputFields)
    ) {
        //Recipe Name
        UserFieldInput(
            label = "Name",
            value = nameState,
            onValueChange = {
                nameState = it
                isNameValid = isValidString(it)
                if (isNameValid) { recipe.name = nameState }
            },
            isInvalid = !isNameValid,
            errorMessage = invalidStringErrorMessage,
            modifier = Modifier.fillMaxWidth()
        )

        //Ingredients
        IngredientDisplay(
            recipe = recipe,
            toggleDisplayInputRow = toggleDisplayInputRow,
            displayInputRow = displayInputRow
        )

        //Portion Yield
        UserFieldInput(
            label = "Portion yield",
            value = portionState,
            onValueChange = {
                portionState = it
                isPortionValid = isValidPositiveInteger(it)
                if (isPortionValid) { recipe.portionYield = it.toInt() }
            },
            isInvalid = !isPortionValid,
            errorMessage = invalidIntegerErrorMessage,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        //Web URL
        UserFieldInput(
            label = "Web URL",
            value = urlState,
            onValueChange = {
                urlState = it
                recipe.webURL = urlState
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Displays a pop up message where users can dismiss or confirm the requested action.
 *
 * @param onDismissRequest Actions to take when the user clicks outside of the popup or the dismiss button
 * @param onConfirmation Actions to take when the users click on the confirm button
 * @param confirmText Text to display on the confirm button
 * @param dialogTitle Header of the confirm pop-up window
 * @param dialogText Message of the pop-up window
 * @param icon Icon to display at the top of the pop-up window
 */
@Composable
fun ConfirmPopup(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    confirmText: String,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    /*
    * got this code and modified it a bit from here:
    * https://developer.android.com/jetpack/compose/components/dialog#alert
    */
    AlertDialog(
        icon = { Icon(icon, contentDescription = null) },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
            ) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    )
}

/**
 * Returns if a string is empty or not.
 */
fun isValidString(input: String): Boolean {
    return input.isNotBlank()
}

/**
 * Returns true if a string can be converted to a non-null integer and is above zero.
 */
private fun isValidPositiveInteger(string: String): Boolean {
    return string.toIntOrNull()?.let { parsedInt ->
        parsedInt > 0
    } ?: false
}

/**
 * Displays a text box for a user to fill. If the input is invalid, displays a error message
 * underneath the text box.
 *
 * @param label Label to display on top of the text box
 * @param value Value the field corresponds to
 * @param onValueChange Actions to take when the value of the text box is changed
 * @param isInvalid Whether the user input is valid or not. If true, displays an error message.
 * @param errorMessage Error message to display if the input is invalid.
 * @param keyboardOptions Keyboard options for specific cases. For example, when you want the user to
 * only be able to input numbers.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFieldInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isInvalid: Boolean = false,
    errorMessage: String = "Invalid input",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions : KeyboardActions = KeyboardActions(),
    containerColor : Color = MaterialTheme.colorScheme.surface
) {
    //Temporary hardcoded values - to be modified when implementing responsive behavior
    val rightPadding = 150.dp

    TextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(15.dp),
        label = { Text(text = label, fontWeight = FontWeight.Medium) },
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

    if (isInvalid) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 12.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(end = rightPadding)
        )
    }
}

/**
 * Contains the buttons for a user to either:
 * - Save the changes to the current recipe
 * - Delete the current recipe
 *
 * Each button displays a confirmation popup before proceeding.
 * @param recipe
 * @param recipeViewModel
 * @param originalRecipe Original state of the recipe when the user first came on this screen. Used
 * for the ViewModel to find the recipe even if the name was edited.
 * @param goToRecipeList Navigation to Recipe List screen once the action of a button was performed.
 */
@Composable
fun ButtonRow(
    recipe: Recipe,
    recipeViewModel: RecipeViewModel,
    modifier: Modifier = Modifier,
    originalRecipe: Recipe,
    goToRecipeList: () -> Unit
){
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }

    var confirmedDelete by remember { mutableStateOf(false) }

    //Temporary hardcoded values - to be modified when implementing responsive behavior
    val spaceBetweenButtons = 4.dp

    Row(modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = { showSaveDialog = true },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Save"
            )
        }

        Spacer(modifier = Modifier.width(spaceBetweenButtons))

        IconButton(
            onClick = { showDeleteDialog = true },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete"
            )
        }
    }

    if (showDeleteDialog) {
        if (confirmedDelete) {
            LaunchedEffect(Unit) {
                recipeViewModel.removeRecipe(recipe)
                goToRecipeList()
            }
        } else {
            ConfirmPopup(
                onDismissRequest = { showDeleteDialog = false },
                onConfirmation = { confirmedDelete = true },
                dialogTitle = "Are you sure?",
                dialogText = "Would you like to delete ${recipe.name}? This cannot be undone.",
                icon = Icons.Default.Warning,
                confirmText = "Delete"
            )
        }
    }

    if (showSaveDialog){
        ConfirmPopup(
            onDismissRequest = { showSaveDialog = true },
            onConfirmation = {
                recipeViewModel.editRecipe(originalRecipe.name, recipe)
                goToRecipeList()
            },
            dialogTitle = "Save changes",
            dialogText = "Would you like to save your changes to " + recipe.name + "?",
            icon = Icons.Default.Edit,
            confirmText = "Save"
        )
    }
}

/**
 * Displays a list of the current recipe's ingredients as well as a button to add a new ingredient
 * to the recipe. Once clicked, the button toggles a row of fields where users can add the missing
 * information the new ingredient.
 *
 * @param recipe
 * @param toggleDisplayInputRow Function to toggle the visibility of the new ingredient input row
 * @param displayInputRow If true, displays the row of input fields to add a new ingredient to
 *  * the recipe
 */
@Composable
fun IngredientDisplay(
    recipe: Recipe,
    toggleDisplayInputRow: () -> Unit,
    displayInputRow: Boolean
) {
    //Temporary hardcoded values - to be modified when implementing responsive behavior
    val roundedCornerRadius = 20.dp
    val minColumnHeight = 150.dp
    val maxColumnHeight = 350.dp
    val columnPadding = 20.dp
    val spacerHeight = 20.dp

    // State to hold the list of ingredients
    var ingredients by remember { mutableStateOf(recipe.ingredients.toMutableList()) }

    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(roundedCornerRadius)
            )
    ) {
        Column(
            modifier = Modifier
                .defaultMinSize(minColumnHeight)
                .heightIn(minColumnHeight, maxColumnHeight)
                .padding(columnPadding)
        ) {
            LazyColumn {
                items(
                    items = ingredients,
                    key = { ingredient -> ingredient.name }
                ) { ingredient ->
                    key(ingredient.name) {
                        IngredientDisplayRow(ingredient = ingredient)
                    }
                }
            }

            Spacer(Modifier.height(spacerHeight))

            // AddIngredientButton or IngredientInputRow
            if (displayInputRow) {
                IngredientInputRow(
                    onIngredientAdded = { newIngredient ->
                        ingredients = ingredients.toMutableList().apply {
                            add(newIngredient)
                        }
                        toggleDisplayInputRow()
                        recipe.ingredients.add(newIngredient)
                    }
                )
            } else {
                AddIngredientButton(onClick = toggleDisplayInputRow)
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
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                "Add Ingredient",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Displays a single ingredient's quantity, measurement and name.
 */
@Composable
fun IngredientDisplayRow(
    ingredient: TemporaryIngredient
) {
    //Temporary hardcoded values - to be modified when implementing responsive behavior
    val spacerWidth = 16.dp

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            ingredient.quantity.toString(),
            modifier = Modifier.weight(0.7f),
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.weight(0.3f))

        Text(
            ingredient.measurement.abbreviation,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier
            .weight(0.5f)
            .width(spacerWidth)
        )

        Text(
            ingredient.name,
            modifier = Modifier.weight(3f),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Row of input fields for users to fill out when creating a new ingredient to be added to a recipe.
 */
@Composable
fun IngredientInputRow(
    onIngredientAdded: (TemporaryIngredient) -> Unit
) {
    var ingredientQuantity by remember { mutableStateOf("") }
    var ingredientMeasurement by remember { mutableStateOf(Measurements.NONE) }
    var ingredientName by remember { mutableStateOf("") }

    //Temporary hardcoded values - to be modified when implementing responsive behavior
    val quantityFieldWidth = 75.dp
    val nameFieldWidth = 150.dp
    val spaceInBetween = 13.dp
    val dropdownMenuOffset = 0.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceInBetween)
    ) {
        //Quantity
        UserFieldInput(
            label = "Qty",
            value = ingredientQuantity,
            onValueChange = { ingredientQuantity = it },
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .width(quantityFieldWidth),
            keyboardOptions = KeyboardOptions.Default.copy(),
            containerColor = MaterialTheme.colorScheme.background
        )
        //Measurement
        DropdownMeasurement(
            modifier = Modifier
                .offset(y = dropdownMenuOffset)
                .weight(0.5f)
                .fillMaxHeight(),
            onValueChange = { ingredientMeasurement = it }
        )

        //Name
        UserFieldInput(
            label = "Name",
            value = ingredientName,
            onValueChange = { ingredientName = it },
            modifier = Modifier
                .fillMaxHeight()
                .weight(2f)
                .width(nameFieldWidth),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Recompose IngredientsDisplay to show new changes
                    onIngredientAdded(TemporaryIngredient(
                        name = ingredientName,
                        quantity = ingredientQuantity.toDouble(),
                        measurement = ingredientMeasurement
                    ))

                    // Reset input fields after adding ingredient
                    ingredientQuantity = ""
                    ingredientName = ""
                }
            ),
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}

/**
 * Dropdown menu that displays all measurement options to the user.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMeasurement(
    modifier: Modifier,
    onValueChange: (Measurements) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedMeasurement by remember { mutableStateOf(Measurements.NONE) }

    //Taken from https://alexzh.com/jetpack-compose-dropdownmenu/
    Box(
        modifier = Modifier
            .width(100.dp)
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
                            onValueChange(selectedMeasurement)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}