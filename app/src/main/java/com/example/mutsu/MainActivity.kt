package com.example.mutsu

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.navigation.compose.rememberNavController
import com.example.mutsu.loginRegistration.AuthViewModel
import com.example.mutsu.loginRegistration.AuthViewModelFactory
import com.example.mutsu.loginRegistration.LoginRegisterScreen
import com.example.mutsu.loginRegistration.Users
import com.example.mutsu.navigation.AboutUs
import com.example.mutsu.navigation.GroceryList
import com.example.mutsu.navigation.Home
import com.example.mutsu.navigation.LoginRegister
import com.example.mutsu.navigation.MealPlan
import com.example.mutsu.navigation.RecipeInformation
import com.example.mutsu.navigation.Recipes
import com.example.mutsu.ui.theme.MutsuTheme
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState

private const val INGREDIENTS_NAME_FILE = "ingredients_name"
private const val RECIPES_FILE = "stored_recipes"
private val dataStores = DataStoreSingleton()

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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            MutsuApp(windowSizeClass)
        }
    }

    @Composable
    fun MutsuApp(windowSize: WindowSizeClass){
        val navController = rememberNavController()

        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination

        val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
        val currentUser = authViewModel.currentUser().collectAsState()

        MutsuTheme{
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                when (windowSize.widthSizeClass){
                    WindowWidthSizeClass.Compact -> {
                        MutsuAppPortrait(
                            navController,
                            currentUser,
                            authViewModel,
                            windowSize
                        )
                    }
                    else -> {
                        MutsuAppLandscape(
                            navController,
                            currentUser,
                            authViewModel,
                            windowSize
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun MutsuAppLandscape(
        navController: NavHostController,
        currentUser: State<Users?>,
        authViewModel: AuthViewModel,
        windowSize: WindowSizeClass
    ) {
        Row {
            MutsuNavigationRail(
                modifier = Modifier,
                navController = navController,
                currentUser = currentUser
            )
            HomeScreen(
                modifier = Modifier,
                navController = navController,
                authViewModel = authViewModel,
                windowSize = windowSize
            )
        }
    }

    @Composable
    fun MutsuAppPortrait(
        navController: NavHostController,
        currentUser: State<Users?>,
        authViewModel: AuthViewModel,
        windowSize: WindowSizeClass
    ) {
        Scaffold(
            topBar = { MutsuTopAppBar() },
            bottomBar = {
                MutsuBottomNavigation(
                    navController = navController,
                    currentUser = currentUser
                )
            }
        ) {innerPadding ->
            HomeScreen(
                navController,
                authViewModel,
                Modifier.padding(innerPadding),
                windowSize
            )
        }
    }

    @Composable
    fun HomeScreen(
        navController : NavHostController,
        authViewModel : AuthViewModel,
        modifier: Modifier = Modifier,
        windowSize: WindowSizeClass
    ) {
        val recipeViewModel = RecipeViewModel(recipesStore, this)
        val ingredientsViewModel = IngredientsViewModel(
            dataStore = ingredientsNameStore,
            mealPlanStore = mealPlanStore,
            recipeStore = recipesStore,
            context = this
        )
        val mealsViewModel = MealsViewModel(recipesStore, mealPlanStore, this)

        NavHost(
            navController = navController,
            startDestination = Home.route,
            modifier = modifier
        ) {

            /*
             *  In each of these composables, they have four extra arguments:
             *      enterTransition
             *      exitTransition
             *      popEnterTransition
             *      popExitTransition
             *  These are used when transitioning between screens. Each of them have a
             *  direction for the screen to move in and a tween for how long it should
             *  be moving. This allows for a smooth animation while transitioning from
             *  screen to screen.
             *
             *  This part of the code comes from the tutorial linked here:
             *  https://proandroiddev.com/screen-transition-animations-with-jetpack-navigation-17afdc714d0e
             */

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
                    },
                    windowSize
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
                LoginRegisterScreen(authViewModel, windowSize)
            }
        }
    }

    @Composable
    private fun MutsuBottomNavigation(
        navController : NavHostController,
        currentUser: State<Users?>
    ) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navController.navigateSingleTopTo(Home.route) }
                ) {
                    Icon(Home.icon, contentDescription = "Home")
                }

                if (currentUser.value != null) {
                    IconButton(
                        onClick = { navController.navigateSingleTopTo(Recipes.route) }
                    ) {
                        Icon(Recipes.icon, contentDescription = "Recipes")
                    }

                    IconButton(
                        onClick = { navController.navigateSingleTopTo(MealPlan.route) }
                    ) {
                        Icon(MealPlan.icon, contentDescription = "Meal Plan")
                    }
                }

                IconButton(onClick = { navController.navigateSingleTopTo(LoginRegister.route) }) {
                    Icon(LoginRegister.icon, contentDescription = "Login/Register")
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MutsuTopAppBar(){
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.applecore),
                    contentDescription = "Apple core icon",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Mutsu",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun MutsuNavigationRail(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentUser: State<Users?>
) {
    NavigationRail(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Home.icon,
                        contentDescription = "Home"
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = Color.Transparent,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = Color.Transparent,
                    disabledIconColor = MaterialTheme.colorScheme.onPrimary,
                    disabledTextColor = Color.Transparent
                ),
                selected = false,
                onClick = { navController.navigateSingleTopTo(Home.route) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (currentUser.value != null) {
                NavigationRailItem(
                    icon = {
                        Icon(
                            imageVector = Recipes.icon,
                            contentDescription = "Recipe Collection"
                        )
                    },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = Color.Transparent,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedTextColor = Color.Transparent,
                        disabledIconColor = MaterialTheme.colorScheme.onPrimary,
                        disabledTextColor = Color.Transparent
                    ),
                    selected = false,
                    onClick = { navController.navigateSingleTopTo(Recipes.route) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                NavigationRailItem(
                    icon = {
                        Icon(
                            imageVector = MealPlan.icon,
                            contentDescription = "Meal Plan"
                        )
                    },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = Color.Transparent,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedTextColor = Color.Transparent,
                        disabledIconColor = MaterialTheme.colorScheme.onPrimary,
                        disabledTextColor = Color.Transparent
                    ),
                    selected = false,
                    onClick = { navController.navigateSingleTopTo(MealPlan.route) }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = LoginRegister.icon,
                        contentDescription = "Log in and Register"
                    )
                },
                selected = false,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = Color.Transparent,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = Color.Transparent,
                    disabledIconColor = MaterialTheme.colorScheme.onPrimary,
                    disabledTextColor = Color.Transparent
                ),
                onClick = { navController.navigateSingleTopTo(LoginRegister.route) }
            )
        }
    }
}

private fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) { launchSingleTop = true }
}

private fun NavHostController.navigateToRecipeInformation(recipeName: String) {
    this.navigateSingleTopTo("${RecipeInformation.route}/$recipeName")}
}
