package com.example.mutsu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MealPlanScreen(mealsViewModel: MealsViewModel,modifier: Modifier = Modifier) {
    val meals by mealsViewModel.meals.collectAsStateWithLifecycle()

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
    modifier: Modifier = Modifier){
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

    Button(
        onClick = { },
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