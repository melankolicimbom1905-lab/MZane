package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.ThemeType

private val TitaniumDarkScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.Black,
    secondary = DarkSecondary,
    onSecondary = Color.White,
    tertiary = DarkAccent,
    background = DarkBg,
    onBackground = Color(0xFFF1F5F9),
    surface = DarkSurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFF94A3B8)
)

private val AmoledDarkScheme = darkColorScheme(
    primary = AmoledPrimary,
    onPrimary = Color.Black,
    secondary = Color(0xFF38BDF8),
    tertiary = Color(0xFFF59E0B),
    background = AmoledBg,
    onBackground = Color.White,
    surface = AmoledSurface,
    onSurface = Color.White,
    surfaceVariant = AmoledCard,
    onSurfaceVariant = Color(0xFFA1A1AA)
)

private val OceanBlueScheme = darkColorScheme(
    primary = OceanPrimary,
    onPrimary = Color.Black,
    secondary = Color(0xFF38BDF8),
    tertiary = Color(0xFF34D399),
    background = OceanBg,
    onBackground = Color(0xFFE2E8F0),
    surface = OceanSurface,
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = OceanCard,
    onSurfaceVariant = Color(0xFF94A3B8)
)

private val CyberpunkScheme = darkColorScheme(
    primary = CyberPrimary,
    onPrimary = Color.White,
    secondary = CyberAccent,
    tertiary = Color(0xFFFFD600),
    background = CyberBg,
    onBackground = Color(0xFFFAFAFA),
    surface = CyberSurface,
    onSurface = Color(0xFFFAFAFA),
    surfaceVariant = CyberCard,
    onSurfaceVariant = Color(0xFFE0E7FF)
)

private val GoldVipScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color.Black,
    secondary = GoldAccent,
    tertiary = Color(0xFFFBBF24),
    background = GoldBg,
    onBackground = Color(0xFFFFFBEB),
    surface = GoldSurface,
    onSurface = Color(0xFFFFFBEB),
    surfaceVariant = GoldCard,
    onSurfaceVariant = Color(0xFFD97706)
)

@Composable
fun MyApplicationTheme(
    selectedTheme: ThemeType = ThemeType.TITANIUM_DARK,
    content: @Composable () -> Unit
) {
    val colorScheme = when (selectedTheme) {
        ThemeType.TITANIUM_DARK -> TitaniumDarkScheme
        ThemeType.AMOLED -> AmoledDarkScheme
        ThemeType.OCEAN_BLUE -> OceanBlueScheme
        ThemeType.CYBERPUNK -> CyberpunkScheme
        ThemeType.GOLD_VIP -> GoldVipScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
