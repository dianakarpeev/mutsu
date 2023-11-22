package com.example.emptyactivity.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.emptyactivity.AboutUsScreen
import com.example.emptyactivity.IngredientsScreen
import com.example.emptyactivity.MutsuHomeScreen
import com.example.emptyactivity.RecipeListScreen
import com.example.emptyactivity.foodCounter

/*
 * Set up this file based on the following codelab:
 * https://developer.android.com/codelabs/jetpack-compose-navigation#2
 */

interface MutsuDestination {
    val icon: ImageVector
    val route: String
}

object Home : MutsuDestination {
    override val icon = Icons.Filled.Home
    override val route = "home"
}

object GroceryList : MutsuDestination {
    override val icon = Icons.Filled.Star
    override val route = "grocery-list"
}

object MealPlan : MutsuDestination {
    override val icon = Icons.Filled.ShoppingCart
    override val route = "meal-plan"
}

object Recipes : MutsuDestination {
    override val icon = Icons.Filled.Star
    override val route = "recipe-list"
}

object AboutUs : MutsuDestination {
    override val icon = Icons.Filled.Star
    override val route = "about-us"
}

val mustuTabRowScreens = listOf(Home, Recipes, MealPlan, GroceryList)