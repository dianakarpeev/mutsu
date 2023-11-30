package com.example.mutsu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.mutsu.ui.theme.MutsuTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mutsu.loginRegistration.LoginRegisterScreen
import com.example.mutsu.navigation.AboutUs
import com.example.mutsu.navigation.GroceryList
import com.example.mutsu.navigation.Home
import com.example.mutsu.navigation.LoginRegister
import com.example.mutsu.navigation.MealPlan
import com.example.mutsu.navigation.Recipes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MutsuApp()
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class,
        ExperimentalMaterial3Api::class)
    @Composable
    fun MutsuApp(){
        MutsuTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val navController = rememberNavController()

                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination

                val windowSizeClass = calculateWindowSizeClass(this)
                val ingredientsViewModel : IngredientsViewModel = viewModel()

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
                                IconButton(
                                    onClick = { navController.navigateSingleTopTo(LoginRegister.route) }
                                ) {
                                    Icon(LoginRegister.icon, contentDescription = "Login/Register")
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
                            MutsuHomeScreen(
                                goToAboutUs = {
                                    navController.navigateSingleTopTo(AboutUs.route)
                                }
                            )
                        }
                        composable(route = MealPlan.route){
                            foodCounter()
                        }
                        composable(route = Recipes.route){
                            RecipeListScreen(windowSizeClass)
                        }
                        composable(route = GroceryList.route){
                            IngredientsScreen(ingredientsViewModel)
                        }
                        composable(route = AboutUs.route){
                            AboutUsScreen()
                        }
                        composable(route = LoginRegister.route){
                            LoginRegisterScreen()
                        }
                    }
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }