package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryCyan,
    onPrimary = Color(0xFF003834),
    primaryContainer = Color(0xFF00504B),
    onPrimaryContainer = Color(0xFF6FF7EC),
    secondary = AccentSkyBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF004C6D),
    onSecondaryContainer = Color(0xFFCBE6FF),
    tertiary = MobileGreen,
    onTertiary = Color.White,
    background = DarkBg,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextGrayLight,
    outline = DarkBorder,
    error = StatusDanger
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryCyanDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCEFAF4),
    onPrimaryContainer = Color(0xFF00201D),
    secondary = AccentSkyBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1E4FF),
    onSecondaryContainer = Color(0xFF001D35),
    tertiary = MobileGreen,
    onTertiary = Color.White,
    background = LightBg,
    onBackground = TextDark,
    surface = LightSurface,
    onSurface = TextDark,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = TextDarkSecondary,
    outline = LightBorder,
    error = StatusDanger
)

@Composable
fun DataGuardTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
