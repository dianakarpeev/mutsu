package com.example.mutsu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MealPlanScreen(goToGroceryListScreen : () -> Unit, mealsViewModel: MealsViewModel,modifier: Modifier = Modifier) {
    val meals by mealsViewModel.meals.collectAsStateWithLifecycle()

    val createMealPlan: () -> Unit = {
        mealsViewModel.addMealPlan()
    }

    val increase: (Int) -> Unit = {
        mealsViewModel.increaseQuantity(it)
    }
    val decrease: (Int) -> Unit = {
        mealsViewModel.decreaseQuantity(it)
    }

    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ){
        Instructions(modifier)
        ShowAllMeals(meals, increase, decrease, modifier)
        GroceryListButton(goToGroceryListScreen, modifier, createMealPlan)
    }
}

data class Meals(var recipe: Recipe, var quantity: Int)

@Composable
fun Instructions(modifier: Modifier = Modifier){
    Column(){
        Text(
            text = "Meal Plan",
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(6.dp)
        )
        Text(
            text = "Plan for the upcoming week! \nSelect the meals you'd like to prepare and Mutsu will generate a grocery list for you. Beware! Leaving this screen will make you lose your changes.",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(6.dp)
        )
    }
}

@Composable
fun ShowAllMeals(
    meals: List<Meals>,
    increaseQuantity: (Int) -> Unit,
    decreaseQuantity: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier,){
        meals.forEachIndexed { index, it ->
            MealBox(
                mealName = it.recipe.name,
                mealQuantity = it.quantity,
                isAdded = { increaseQuantity(index) },
                isRemoved = { if (it.quantity > 0) decreaseQuantity(index) },
                modifier = modifier
            )
        }
    }


}

@Composable
fun GroceryListButton(goToGroceryListScreen: () -> Unit, modifier: Modifier = Modifier, createMealPlan : () -> Unit){
    var showPopUp by rememberSaveable { mutableStateOf(false) }

    Button(
        onClick = { showPopUp = true },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier.padding(10.dp)
    ){
        Text(
            text = "Go to the Grocery List!",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }

    if (showPopUp){
        MealPlanConfirmationPopUp(
            confirm = {
                showPopUp = false
                createMealPlan()
                goToGroceryListScreen()
                      },
            dismiss = { showPopUp = false }
        )
    }
}

@Composable
fun MealBox(
    mealName : String,
    mealQuantity : Int,
    isAdded : () -> Unit,
    isRemoved: () -> Unit,
    modifier : Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 13.dp, end = 10.dp, bottom = 0.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Row(
                modifier = modifier.padding(start = 8.dp, top = 10.dp, end = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = mealName,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = isAdded,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier.padding(
                        start = 0.dp,
                        top = 0.dp,
                        end = 15.dp,
                        bottom = 0.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add one meal name"
                    )
                }

                Text(
                    text = "$mealQuantity",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )

                IconButton(
                    onClick = isRemoved,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier.padding(
                        start = 15.dp,
                        top = 0.dp,
                        end = 0.dp,
                        bottom = 0.dp
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.remove_icon),
                        contentDescription = "remove one meal name"
                    )
                }
            }
        }
    }
}

@Composable
fun MealPlanConfirmationPopUp(
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
            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
        },
        title = {
            Text("Go to the Grocery List")
        },
        text = {
            Text("Are you sure you want to go to the grocery list screen? You won't be able to make any changes to your meal plan once you proceed.")
        },
        onDismissRequest = {
            dismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
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