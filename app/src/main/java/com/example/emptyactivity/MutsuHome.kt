package com.example.emptyactivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MutsuHomeScreen(modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(text = "Mutsu",
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier)
        Text(text = "Your Grocery Companion")
    }
}