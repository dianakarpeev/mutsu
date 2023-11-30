package com.example.mutsu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Box
import com.example.mutsu.ui.theme.MutsuTheme

data class Recipe(val name: String)


@Composable
fun RecipeListScreen(windowSizeClass: WindowSizeClass, modifier: Modifier = Modifier){
    CookbookApp(windowSizeClass)
}

//Contains the textbox that accepts user input and the button that saves it!
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeInput(
    recipeName: String,
    onRecipeNameChange: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    modifier : Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = recipeName,
            onValueChange = { onRecipeNameChange(it) },
            label = { Text("Add New Recipe") },
            placeholder = { Text("Recipe Name") },
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = { onAddButtonClick() },
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(vertical = 8.dp)
        ) {
            Text("Add")
        }
    }
}

//Creates sections (user input and recipe grid) with titles to make it easier on the eyes.
@Composable
fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .paddingFromBaseline(top = 40.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        )
        content()
    }
}

/**
 * Grid to display all recipe names entered by the user using cards.
 * If no recipes were entered, it'll display a simple text message.
 */
@Composable
fun RecipeList(recipeList: List<Recipe>) {
    if (recipeList.isNotEmpty()){
        Box (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(recipeList) { recipe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(width = 240.dp, height = 65.dp)
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = recipe.name,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    } 
    else {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No recipes to display",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.LightGray
        )
    }
}

//Version of the app that uses the horizontal navigation bar
@Composable
fun CookBookPortrait(){
    MutsuTheme {
        Scaffold(
            bottomBar = { BottomNavigation() }
        ) { padding ->
            HomeScreen(Modifier.padding(padding))
        }
    }
}

//Layout of the main screen, calls all elements
@Composable
fun HomeScreen(modifier: Modifier = Modifier){
    val viewModel : CookbookViewModel = viewModel()
    var recipeName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        RecipeInput(
            recipeName = recipeName,
            onRecipeNameChange = { newRecipeName -> recipeName = newRecipeName },
            onAddButtonClick = {
                if (recipeName.isNotEmpty()) {
                    viewModel.addRecipe(Recipe(recipeName))
                    recipeName = ""
                }
            },
            modifier = Modifier
                .weight(1f)
        )

        Section(
            title = "Existing Recipes",
            modifier = Modifier.weight(3f)
        ){
            RecipeList(viewModel.recipeList)
        }
    }
}

//Version of the app that uses the vertical navigation bar
@Composable
fun CookBookLandscape(){
    MutsuTheme {
        Surface(color = MaterialTheme.colorScheme.background){
            Row{
                NavigationRail()
                HomeScreen()
            }
        }
    }
}

//Chooses the navigation bar to use depending on the current window size
@Composable
fun CookbookApp(windowSize: WindowSizeClass) {
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            CookBookPortrait()
        }
        WindowWidthSizeClass.Expanded -> {
            CookBookLandscape()
        }
    }
}

@Composable
private fun BottomNavigation(modifier: Modifier = Modifier) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = "Favorites"
                )
            },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = "Home"
                )
            },
            selected = false,
            onClick = {}
        )
    }
}

@Composable
private fun NavigationRail(modifier: Modifier = Modifier) {
    NavigationRail(
        modifier = modifier.padding(start = 8.dp, end = 8.dp),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null
                    )
                },
                label = {
                    Text("Favorites")
                },
                selected = true,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            NavigationRailItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null
                    )
                },
                label = {
                    Text("Home")
                },
                selected = false,
                onClick = {}
            )
        }
    }
}

