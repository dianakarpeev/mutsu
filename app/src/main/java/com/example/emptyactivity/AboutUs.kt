package com.example.emptyactivity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun AboutUsScreen(modifier: Modifier = Modifier){
    Column(modifier = modifier.verticalScroll(rememberScrollState()).fillMaxWidth()){
        Text(text = "Keeping track of what to eat and what ingredients are necessary for these meals can be difficult...but we're here to change that.",
            modifier = modifier.padding(6.dp,0.dp),)
        Text(text = "Meet the creators!",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(6.dp),)
        CreatorInformation(name = "Breanna", description = "A short description of me should go here.")
        CreatorInformation(name = "Diana", description = "A short description of Diana should go here.")
        CreatorInformation(name = "Mel", description = "A short description of Mel should go here.")
    }
}

@Composable
fun CreatorInformation(name: String, description: String, modifier: Modifier = Modifier){
    Row(modifier.fillMaxWidth()){
        Column(
            modifier
                .fillMaxWidth(0.3f)
                .padding(6.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                    shape = RectangleShape
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            Text(text = "*maybe add image here*", modifier.padding(6.dp))
        }
        Column(modifier.fillMaxWidth()){
            Text(text = name,
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(6.dp))
            Text(text = description, modifier = modifier.padding(6.dp, 0.dp))
        }
    }

}