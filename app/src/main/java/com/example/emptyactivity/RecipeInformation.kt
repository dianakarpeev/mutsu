package com.example.emptyactivity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun RecipeInformation(modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        ButtonRow()
        RecipeForm()
    }
}

@Composable
fun RecipeForm(modifier: Modifier = Modifier){
    var recipeName by remember { mutableStateOf("") }
    var webURL by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        //Recipe Name
        UserInput("Name", recipeName)
        Spacer(modifier = Modifier.height(200.dp))

        //Ingredients
        Text(text = "Ingredients")
        Spacer(modifier = Modifier.height(200.dp))

        //Web URL
        UserInput("Web URL", webURL)
        Spacer(modifier = Modifier.height(200.dp))

        //Image
        Text(text = "Image")
        PlaceHolder(modifier, "User image upload here.")
    }
}

@Composable
fun UserInput(label: String, input: String){
    Text(text = label)
    TextField(
        value = input,
        onValueChange = { }
    )
}

@Composable
fun PlaceHolder(modifier: Modifier = Modifier, text: String){
    Row(
        modifier = modifier
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                shape = RectangleShape
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(12.dp)
        )
    }
}

@Composable
fun ButtonRow(modifier: Modifier = Modifier){
    Row(modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ){
        Button(onClick = {}, modifier = modifier.padding(0.dp, 5.dp)) {
            Text(text = "Edit")
        }
        Button(onClick = { }, modifier = modifier.padding(0.dp, 5.dp)) {
            Text(text = "Delete")
        }
    }
}