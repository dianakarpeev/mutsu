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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun MutsuHomeScreen(changeScreen: () -> Unit, modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Row(modifier = modifier
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                shape = RectangleShape
            ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
            Text(text = "*image icon goes here*",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(12.dp))
        }
        Text(text = "Welcome to Mutsu!",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(6.dp),)
        Text(text = "Your Grocery Companion", modifier,)
        Button(onClick = changeScreen, modifier = modifier.padding(0.dp, 25.dp),) {
            Text(text = "To the About Us page!")
        }
    }
}