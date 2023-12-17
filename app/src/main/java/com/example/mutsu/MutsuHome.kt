package com.example.mutsu


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MutsuHomeScreen(
    goToAboutUs: () -> Unit,
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                HomePortrait(goToAboutUs = goToAboutUs, modifier)
            }
            else -> {
                HomeLandscape(goToAboutUs = goToAboutUs, modifier)
            }
        }
    }
}

@Composable
fun HomePortrait(
    goToAboutUs: () -> Unit,
    modifier: Modifier = Modifier
){
    Spacer(modifier = modifier.height(10.dp))

    Image(
        painter = painterResource(id = R.drawable.mutsu),
        contentDescription = null,
        Modifier.size(size = 270.dp)
    )

    Spacer(modifier = modifier.height(10.dp))

    Text(
        text = "Welcome to Mutsu!",
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(6.dp)
    )

    Text(
        text = "Your Grocery Companion",
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .padding(17.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Figuring out what to eat and what ingredients you need can be challenging. " +
                    "We're here to change that!",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(20.dp)
        )
    }

    Button(
        onClick = goToAboutUs,
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.background
        )
    ) {
        Text(
            text = "About Us",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun HomeLandscape(
    goToAboutUs: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.mutsu),
            contentDescription = null,
            Modifier.size(size = 270.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Welcome to Mutsu!",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(6.dp)
            )

            Text(
                text = "Your Grocery Companion",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .padding(17.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Figuring out what to eat and what ingredients you need can be challenging. " +
                            "We're here to change that!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .padding(20.dp)
                )
            }

            Button(
                onClick = goToAboutUs,
                modifier = modifier
                    .fillMaxHeight()
                    .padding(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(
                    text = "About Us",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}