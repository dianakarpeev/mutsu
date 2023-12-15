package com.example.mutsu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Displays information about the team behind the Mutsu project! Shows our names, a food
 * icon we relate to and our relationship with cooking. Users can click on our names to expand the
 * menu and display a short paragraph.
 *
 * @param modifier The modifier for styling and layout of the screen.
 */
@Composable
fun AboutUsScreen(modifier: Modifier = Modifier){
    val horizontalPadding = 20.dp
    val verticalPadding = 50.dp
    val spacedBetweenHeaders = 20.dp
    val spaceBetweenSections = 40.dp
    val spaceBetweenContainers = 20.dp

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontalPadding, verticalPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Behind Mutsu",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier.height(spacedBetweenHeaders))

        Text(
            text = "We're a team of graduating Computer Science students behind Mutsu, our school " +
                    "project. Our aim is to make grocery shopping and meal planning easier. Please " +
                    "enjoy!",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier.height(spaceBetweenSections))

        CreatorCard(
            image = painterResource(id = R.drawable.blueberry),
            name = "Breanna",
            description = "While I might not cook very often, when I do, my go-to meals are swedish " +
                    "meatballs or any type of poutine. There is just something about both of these " +
                    "foods that I’m obsessed with. Hope you enjoy our app!"
        )

        Spacer(modifier.height(spaceBetweenContainers))

        CreatorCard(
            image = painterResource(id = R.drawable.egg),
            name = "Mel",
            description = "As a broke college student, I'm trying to break my habits of ordering " +
                    "takeout with the fulfilling activity of making my own meals, and Mutsu was just " +
                    "the kind of tool I needed to get started. Bon appétit all!"
        )

        Spacer(modifier.height(spaceBetweenContainers))

        CreatorCard(
            image = painterResource(id = R.drawable.lemon),
            name = "Diana",
            description = "Through cooking, I'm slowly finding joy in food beyond just fuel. Trying new recipes each " +
                    "week turns shopping into an adventure! I hope to share " +
                    "the joy of meal planning stress-free with others."
        )
    }
}

/**
 * Displays an expandable card representing a creator or team member with an image, name, and expandable
 * description.
 *
 * @param image The image of the creator.
 * @param name The name of the creator.
 * @param description The description or bio of the creator.
 */
@Composable
fun CreatorCard(
    image: Painter,
    name: String,
    description: String
) {
    var isExpanded by remember { mutableStateOf(false) }

    val padding = 16.dp
    val roundedCornerRadius = 8.dp
    val imageSize = 45.dp
    val spaceBetweenText = 10.dp

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(roundedCornerRadius),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(padding, padding),
            verticalArrangement = Arrangement.spacedBy(spaceBetweenText),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier.size(imageSize)
                )

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (isExpanded) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
