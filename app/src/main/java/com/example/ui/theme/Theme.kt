package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LocalIsDarkTheme = compositionLocalOf { true }

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = MidnightBg,
    primaryContainer = VividBlue,
    onPrimaryContainer = TextWhite,
    secondary = MintGreen,
    onSecondary = MidnightBg,
    secondaryContainer = MidnightCard,
    onSecondaryContainer = EmeraldGlow,
    tertiary = NeonPurple,
    onTertiary = TextWhite,
    background = MidnightBg,
    onBackground = TextWhite,
    surface = MidnightSurface,
    onSurface = TextWhite,
    surfaceVariant = GlassSurface,
    onSurfaceVariant = TextMuted,
    outline = GlassBorder
)

private val LightColorScheme = lightColorScheme(
    primary = VividBlue,
    onPrimary = TextWhite,
    primaryContainer = LightGlassBorder,
    onPrimaryContainer = VividBlue,
    secondary = MintGreen,
    onSecondary = TextWhite,
    secondaryContainer = LightBg,
    onSecondaryContainer = TextDark,
    tertiary = SoftViolet,
    onTertiary = TextWhite,
    background = LightBg,
    onBackground = TextDark,
    surface = LightSurface,
    onSurface = TextDark,
    surfaceVariant = LightGlassSurface,
    onSurfaceVariant = TextDarkMuted,
    outline = LightGlassBorder
)

@Composable
fun VitalStepTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = colorScheme.background.toArgb()
                it.navigationBarColor = colorScheme.background.toArgb()
                val controller = WindowCompat.getInsetsController(it, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalIsDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
