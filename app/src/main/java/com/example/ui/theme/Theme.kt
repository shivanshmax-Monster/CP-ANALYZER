package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AlgoDarkColorScheme = darkColorScheme(
    primary = ElectricIndigo,
    onPrimary = TextHighEmphasis,
    primaryContainer = SlateElevation,
    onPrimaryContainer = LightIndigo,
    secondary = NeonEmerald,
    onSecondary = ObsidianBg,
    secondaryContainer = EmeraldBg,
    onSecondaryContainer = NeonEmerald,
    tertiary = AmberGold,
    onTertiary = ObsidianBg,
    tertiaryContainer = AmberBg,
    onTertiaryContainer = LightAmber,
    background = ObsidianBg,
    onBackground = TextHighEmphasis,
    surface = DeepZincCard,
    onSurface = TextHighEmphasis,
    surfaceVariant = SlateElevation,
    onSurfaceVariant = TextMediumEmphasis,
    outline = SlateBorder,
    outlineVariant = SlateBorder,
    error = CrimsonRed,
    onError = TextHighEmphasis,
    errorContainer = CrimsonBg,
    onErrorContainer = LightCoral
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AlgoDarkColorScheme,
        typography = Typography,
        content = content
    )
}
