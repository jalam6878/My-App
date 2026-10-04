package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Neon / Electric Palette
val NeonCyan = Color(0xFF00F2FE)
val CyanAccent = Color(0xFF00E5FF)
val ElectricBlue = Color(0xFF4FACFE)
val VividBlue = Color(0xFF2563EB)
val MintGreen = Color(0xFF10B981)
val EmeraldGlow = Color(0xFF34D399)
val NeonPurple = Color(0xFF8B5CF6)
val SoftViolet = Color(0xFFA855F7)
val GoldenSun = Color(0xFFF59E0B)
val FlameOrange = Color(0xFFF97316)
val RoseHeart = Color(0xFFF43F5E)

// Midnight Glass Dark Theme
val MidnightBg = Color(0xFF070B14)
val MidnightSurface = Color(0xFF0D1527)
val MidnightCard = Color(0xFF131F37)
val GlassSurface = Color(0x331E293B)
val GlassSurfaceHighlight = Color(0x55334155)
val GlassBorder = Color(0x3360A5FA)
val GlassBorderGlow = Color(0x6638BDF8)
val GlassCardBg = Color(0x4D111C33)

// Premium White Liquid Glass Light Theme
val LightBg = Color(0xFFF4F7FC)
val LightSurface = Color(0xFFFFFFFF)
val LightGlassSurface = Color(0xD9FFFFFF)
val LightGlassCardBg = Color(0xB8FFFFFF)
val LightGlassCardBorder = Color(0x80CBD5E1)
val LightGlassBorder = Color(0x6638BDF8)
val LightGlassBorderGlow = Color(0x9960A5FA)

// Text Colors
val TextWhite = Color(0xFFFFFFFF)
val TextMuted = Color(0xFF94A3B8)
val TextDark = Color(0xFF0F172A)
val TextDarkMuted = Color(0xFF64748B)

// Gradients
val VitalGradient = Brush.linearGradient(
    colors = listOf(ElectricBlue, NeonCyan)
)

val MintGradient = Brush.linearGradient(
    colors = listOf(NeonCyan, MintGreen)
)

val PurpleGradient = Brush.linearGradient(
    colors = listOf(NeonPurple, SoftViolet)
)

val SunsetGradient = Brush.linearGradient(
    colors = listOf(GoldenSun, FlameOrange)
)

val DarkGlassBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0B132B),
        Color(0xFF070B14),
        Color(0xFF05080F)
    )
)

val WhiteGlassBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFF8FAFC),
        Color(0xFFEEF4FB),
        Color(0xFFE2EAF4)
    )
)

fun getGlassBackground(isDark: Boolean): Brush {
    return if (isDark) DarkGlassBackgroundGradient else WhiteGlassBackgroundGradient
}
