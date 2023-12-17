package com.example.mutsu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mutsu.loginRegistration.AuthViewModel
import com.example.mutsu.loginRegistration.AuthViewModelFactory
import com.example.mutsu.loginRegistration.LoginRegisterScreen
import com.example.mutsu.navigation.AboutUs
import com.example.mutsu.navigation.GroceryList
import com.example.mutsu.navigation.Home
import com.example.mutsu.navigation.LoginRegister
import com.example.mutsu.navigation.MealPlan
import com.example.mutsu.navigation.RecipeInformation
import com.example.mutsu.navigation.Recipes
import com.example.mutsu.ui.theme.MutsuTheme

private const val INGREDIENTS_NAME_FILE = "ingredients_name"
private const val RECIPES_FILE = "stored_recipes"
private val dataStores = dataStoreSingleton()


class MainActivity : ComponentActivity() {

    private val ingredientsNameStore : DataStore<IngredientsName> by lazy {
        dataStores.getIngredientsNameStore(this)
    }

    private val recipesStore : DataStore<StoredRecipes> by lazy {
        dataStores.getRecipesStore(this)
    }

    private val mealPlanStore : DataStore<StoredMealPlan> by lazy {
        dataStores.getMealPlanStore(this)
    }

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

                val recipeViewModel = RecipeViewModel(recipesStore, this)
                val ingredientsViewModel = IngredientsViewModel(ingredientsNameStore, mealPlanStore, recipesStore,this)
                val mealsViewModel = MealsViewModel(recipesStore, mealPlanStore, this)

                val authViewModel : AuthViewModel = viewModel(factory= AuthViewModelFactory())
                var currentUser = authViewModel.currentUser().collectAsState()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(R.drawable.applecore), // Replace with your image resource
                                        contentDescription = "Apple core icon",
                                        modifier = Modifier.size(50.dp) // Adjust the size as needed
                                    )
                                    Spacer(modifier = Modifier.width(10.dp)) // Add spacing between icon and title
                                    Text(
                                        text = "Mutsu",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomAppBar(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = { navController.navigateSingleTopTo(Home.route) }){
                                    Icon(Home.icon, contentDescription = "Home")
                                }
                                if (currentUser.value != null){
                                    IconButton(
                                        onClick = { navController.navigateSingleTopTo(Recipes.route) }
                                    ) {
                                        Icon(Recipes.icon, contentDescription = "Recipes")
                                    }

                                    IconButton(
                                        onClick = { navController.navigateSingleTopTo(MealPlan.route) }
                                    ){
                                        Icon(MealPlan.icon, contentDescription = "Meal Plan")
                                    }
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
                        composable(
                            route = Home.route,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                        ){
                            MutsuHomeScreen(
                                goToAboutUs = {
                                    navController.navigateSingleTopTo(AboutUs.route)
                                }
                            )
                        }
                        composable(
                            route = MealPlan.route,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                        ){
                            MealPlanScreen(
                                goToGroceryListScreen = {
                                    navController.navigateSingleTopTo(GroceryList.route)
                                },
                                mealsViewModel = mealsViewModel
                            )
                        }
                        composable(
                            route = Recipes.route,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                        ){
                            val newRecipeModel = RecipeViewModel(recipesStore, this@MainActivity)
                            RecipeListScreen(
                                goToRecipeInformation = { recipeName ->
                                    navController.navigateToRecipeInformation(recipeName)
                                },
                                recipeViewModel = newRecipeModel
                            )
                        }
                        composable(
                            route = GroceryList.route,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                        ){
                            IngredientsScreen(ingredientsViewModel)
                        }
                        composable(
                            route = AboutUs.route,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                        ){
                            AboutUsScreen()
                        }
                        composable(
                            route = RecipeInformation.routeWithArgs,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            arguments = RecipeInformation.arguments
                        ) { navBackStackEntry ->
                            val recipeName =
                                navBackStackEntry.arguments?.getString(RecipeInformation.recipeNameArg)

                            RecipeInformationScreen(
                                recipeViewModel = recipeViewModel,
                                recipeName = recipeName,
                                goToRecipeList = { navController.navigateSingleTopTo(Recipes.route) }
                            )
                        }
                        composable(
                            route = LoginRegister.route,
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popEnterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                            popExitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                                    animationSpec = tween(1000)
                                )
                            },
                        ){
                            LoginRegisterScreen(authViewModel)
                        }
                    }
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

private fun NavHostController.navigateToRecipeInformation(recipeName: String) {
    this.navigateSingleTopTo("${RecipeInformation.route}/$recipeName")
}
