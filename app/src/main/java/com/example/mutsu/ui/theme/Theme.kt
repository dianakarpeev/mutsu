package com.example.mutsu.ui.theme

import android.app.Activity

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DefaultColorScheme = lightColorScheme(
    primary = Green,
    secondary = Red,
    tertiary = DarkGreen,
    background = LightCream,
    surface = Cream,
    onPrimary = Black,
    onSecondary = LightCream,
    onTertiary = Black,
    onBackground = LightBrown,
    onSurface = Brown,
    errorContainer = DarkRed
)

@Composable
fun MutsuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DefaultColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}