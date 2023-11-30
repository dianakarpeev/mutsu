package com.example.emptyactivity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Screen that displays a list of the user's existing recipes. Users can create recipes by entering
 * a recipe name in the text box and submitting, which will take them to next screen to complete
 * the newly created recipe's information.
 *
 * Users can also click on an existing recipe's card and be redirected to the corresponding recipe's
 * information screen, where they can edit its information or delete it.
 *
 * @param goToRecipeInformation navigation to Recipe Information screen with the recipe name (of the
 * recipe to be shown) as a navigation argument
 */
@Composable
fun RecipeListScreen(goToRecipeInformation: (String) -> Unit){
    val recipeViewModel = RecipeViewModel()

    Column(
        modifier = Modifier
    ) {
        //Region for users to create a new recipe
        RecipeInput(recipeViewModel, goToRecipeInformation)

        //Region for users to view a list of their existing recipes
        Section(
            title = "Existing Recipes",
            modifier = Modifier.weight(3f)
        ){
            RecipeList(
                recipeViewModel.getAllRecipes(),
                goToRecipeInformation
            )
        }
    }
}

private fun addNewEmptyRecipe(recipeViewModel: RecipeViewModel, recipeName: String) {
    recipeViewModel.addRecipe(Recipe(
        name = recipeName,
        ingredients = mutableListOf(),
        portionYield = 0,
        webURL = null
    ))
}

/**
 * Text box and button for users to create a new recipe. Once submitted, the user is redirected
 * to a blank recipe form to fill out the rest of the recipe's information.
 */
@Composable
fun RecipeInput(
    recipeViewModel: RecipeViewModel,
    goToRecipeInformation: (String) -> Unit
) {
    var recipeName by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = recipeName,
            onValueChange = { recipeName = it },
            label = { Text("Add New Recipe") },
            placeholder = { Text("Recipe Name") },
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                if (recipeName.isNotEmpty()) {
                    addNewEmptyRecipe(recipeViewModel, recipeName)
                    goToRecipeInformation(recipeName)
                    recipeName = ""
            } },
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(vertical = 8.dp),
            enabled = false
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeList(recipeList: List<Recipe>, goToRecipeInformation: (String) -> Unit) {
    if (recipeList.isNotEmpty()){
        Box (
            modifier = Modifier//.verticalScroll(rememberScrollState())
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
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
                        ),
                        onClick = { goToRecipeInformation(recipe.name) }
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
