package com.n27.core.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primaryContainer = Colors.LightJet,
    background = Colors.Jet
)

private val LightColorPalette = lightColorScheme(
    primaryContainer = Color.White,
    background = Color.White,
    onBackground = Color.Gray
)

@Composable
fun Theme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(colors, content = content)
}