package com.invoices.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Forest,
    onPrimary = Color.White,
    primaryContainer = ForestContainer,
    onPrimaryContainer = ForestDark,
    secondary = ForestDark,
    onSecondary = Color.White,
    background = Cream,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Color(0xFFEEEAE2),
    onSurfaceVariant = Muted,
    outline = Line,
    error = Danger
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8FD6C0),
    onPrimary = ForestDark,
    primaryContainer = ForestDark,
    onPrimaryContainer = Color(0xFFD8EFE7),
    background = Color(0xFF12100E),
    onBackground = Color(0xFFF5F0E8),
    surface = Color(0xFF1C1917),
    onSurface = Color(0xFFF5F0E8),
    surfaceVariant = Color(0xFF2A2623),
    onSurfaceVariant = Color(0xFFD6D3D1),
    outline = Color(0xFF44403C),
    error = Color(0xFFF87171)
)

@Composable
fun InvoicesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = InvoicesTypography,
        content = content
    )
}
