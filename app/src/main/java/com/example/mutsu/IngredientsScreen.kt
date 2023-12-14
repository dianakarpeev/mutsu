package com.example.mutsu


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun IngredientsScreen(viewModel: IngredientsViewModel = viewModel(), modifier: Modifier = Modifier){
    val ingredients by viewModel.ingredients.collectAsStateWithLifecycle()
    var showGroceryList by rememberSaveable { mutableStateOf(false) }

    val increase: (Int) -> Unit = {
        viewModel.increaseQuantity(it)
    }
    val decrease: (Int) -> Unit = {
        viewModel.decreaseQuantity(it)
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ){
        if (!showGroceryList){
            Instructions("Feel free to add or remove any ingredients found below.", modifier)
            Spacer(modifier.height(10.dp))
            ShowAllIngredients(ingredients, increase, decrease, { showGroceryList = true }, modifier)
        }
        else {
            Instructions("Be careful! Once you leave this screen, the current grocery list will be lost.", modifier)
            Spacer(modifier.height(10.dp))
            ShowGroceryList(ingredients, modifier)
        }
    }
}

/* Data class for the ingredients. */
data class FoodItem (val name: String, var quantityInCart: Int)

/* Prints the instructions for the page at the top of the screen. */
@Composable
fun Instructions(message: String, modifier: Modifier = Modifier){
    Column(){
        Text(
            text = "Grocery List",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(6.dp)
        )
        Text(
            text = "Here is your grocery list. $message",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(6.dp)
        )
    }
}

/* Sets up the saveable remember states for all ingredients as well as calls the function to set up
*  the buttons to add the ingredients to the inventory or grocery list and the buttons to see the lists
*  for each.
*/
@Composable
fun ShowAllIngredients(
    ingredients: List<FoodItem>,
    increaseQuantity: (Int) -> Unit,
    decreaseQuantity: (Int) -> Unit,
    showGroceryList: () -> Unit,
    modifier: Modifier = Modifier
){
    var showPopUp by rememberSaveable { mutableStateOf(false) }

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
            onClick = { showPopUp = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.background
            ),
            modifier = modifier.padding(6.dp)
        ){
            Text(
                text = "Generate List!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }

        if (showPopUp){
            ListConfirmationPopUp(confirm = showGroceryList, dismiss = { showPopUp = false })
        }
    }
}

/* Actually sets up the ingredient name and button and all the logic to add them to either list. */
@Composable
fun IngredientButtonBox(
    ingredientName: String,
    ingredientQuantity: Int,
    isAdded: () -> Unit,
    isRemoved: () -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .padding(6.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically)
        {
            Column(){
                Text(
                    "$ingredientQuantity $ingredientName",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(){
                IconButton(
                    onClick = isAdded,
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
                        contentDescription = "Add one $ingredientName"
                    )
                }

                IconButton(
                    onClick = isRemoved,
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
                        contentDescription = "Remove one $ingredientName"
                    )
                }
            }
        }
    }
}

@Composable
fun ShowGroceryList(ingredients: List<FoodItem>, modifier: Modifier = Modifier){
    Box(modifier = modifier
        .padding(6.dp)
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp)
        )
    ){
        Column(
            modifier = modifier.fillMaxWidth()
                .padding(6.dp),
            ){
            ingredients.forEach{
                if (it.quantityInCart > 0){
                    Text(
                        text = " - ${it.quantityInCart} ${it.name}",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

}

@Composable
fun ListConfirmationPopUp(
    confirm : () -> Unit,
    dismiss : () -> Unit,
    modifier : Modifier = Modifier
){
    /*
    * got this code and modified it a bit from here:
    * https://developer.android.com/jetpack/compose/components/dialog#alert
    */
    AlertDialog(
        icon = {
            Icon(Icons.Filled.List, contentDescription = null)
        },
        title = {
            Text("Generate Grocery List")
        },
        text = {
            Text("Are you sure you want to generate your grocery list? You won't be able to make any edits to the list once you confirm this action.")
        },
        onDismissRequest = {
            dismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dismiss()
                    confirm()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
           ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.background
                ),
            ) {
                Text("Dismiss")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    )
}