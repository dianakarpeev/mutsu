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

interface MutsuDestination {
    val icon: ImageVector
    val route: String
    val screen: @Composable () -> Unit
}

object Home : MutsuDestination {
    override val icon = Icons.Filled.Home
    override val route = "home"
    override val screen : @Composable () -> Unit = { MutsuHomeScreen() }
}

object GroceryList : MutsuDestination {
    override val icon = Icons.Filled.Star
    override val route = "grocery-list"
    override val screen : @Composable () -> Unit = { IngredientsScreen() }
}

object MealPlan : MutsuDestination {
    override val icon = Icons.Filled.ShoppingCart
    override val route = "meal-plan"
    override val screen : @Composable () -> Unit = { foodCounter() }
}

object RecipeList : MutsuDestination {
    override val icon = Icons.Filled.Star
    override val route = "recipe-list"
    override val screen : @Composable () -> Unit = { RecipeListScreen() }
}

object AboutUs : MutsuDestination {
    override val icon = Icons.Filled.Star
    override val route = "about-us"
    override val screen : @Composable () -> Unit = { AboutUsScreen() }
}

val mustuTabRowScreens = listOf(Home, RecipeList, MealPlan, GroceryList)