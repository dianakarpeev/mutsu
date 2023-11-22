package com.example.emptyactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.example.emptyactivity.ui.theme.EmptyActivityTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.emptyactivity.navigation.GroceryList
import com.example.emptyactivity.navigation.Home
import com.example.emptyactivity.navigation.MealPlan
import com.example.emptyactivity.navigation.MutsuDestination
import com.example.emptyactivity.navigation.Recipes
import com.example.emptyactivity.navigation.mustuTabRowScreens

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class,
        ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmptyActivityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by rememberSaveable { mutableStateOf("Home") }
                    Scaffold(
                        topBar = { TopAppBar(title = { Text("MyApp")})},
                        bottomBar = {
                            BottomAppBar {
                                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                                    IconButton(onClick = {currentScreen = "Home"} ) {
                                        Icon(Icons.Filled.Home, contentDescription = "Home")
                                    }
                                    IconButton(onClick = { currentScreen = "Ingredients"} ) {
                                        Icon(Icons.Filled.Star, contentDescription = "Menu")
                                    }
                                    IconButton(onClick = { currentScreen = "Shopping List"} ) {
                                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Add")
                                    }
                                    IconButton(onClick = { currentScreen = "Recipe List" }) {
                                        Icon(Icons.Filled.Menu, contentDescription = "Call")
                                    }
                                }
                            }
                        }
                    ) {

                        //determines which navigation bar to use based on the current window size
                        val windowSizeClass = calculateWindowSizeClass(this)

                        Column(modifier = Modifier.padding(paddingValues = it)) {
                            when (currentScreen) {
                                "Home" -> MutsuHomeScreen({ currentScreen = "About Us" })
                                "About Us" -> AboutUsScreen()
                                "Ingredients" -> IngredientsScreen()
                                "Shopping List" -> foodCounter()
                                "Recipe List" -> RecipeListScreen(windowSizeClass)
                            }
                        }


                    }


                    //here is where we call the different screens
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MutsuApp(){
        EmptyActivityTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()

                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination

                //var currentScreen = mustuTabRowScreens.find { it.route == currentDestination?.route } ?. Home

                Scaffold(
                    topBar = { TopAppBar(title = { Text("MyApp")})},
                    bottomBar = {
                        BottomAppBar {
                            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                                IconButton(
                                    onClick = { navController.navigateSingleTopTo(Home.route) }
                                ){
                                    Icon(Home.icon, contentDescription = "Home")
                                }
                                IconButton(
                                    onClick = { navController.navigateSingleTopTo(GroceryList.route) }
                                ){
                                    Icon(GroceryList.icon, contentDescription = "Grocery List")
                                }
                                IconButton(
                                    onClick = { navController.navigateSingleTopTo(MealPlan.route) }
                                ){
                                    Icon(MealPlan.icon, contentDescription = "Meal Plan")
                                }
                                IconButton(
                                    onClick = { navController.navigateSingleTopTo(Recipes.route) }
                                ) {
                                    Icon(Recipes.icon, contentDescription = "Recipes")
                                }
                            }
                        }
                    }
                ){innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable(route = Home.route){
                            Home.screen()
                        }
                        composable(route = MealPlan.route){
                            MealPlan.screen()
                        }
                        composable(route = Recipes.route){
                            Recipes.screen()
                        }
                        composable(route = GroceryList.route){
                            GroceryList.screen()
                        }
                    }
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }