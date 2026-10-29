package com.miguelzapata.splitsnap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaClaro = lightColorScheme(
    primary = ClaroPrimary,
    onPrimary = ClaroOnPrimary,
    primaryContainer = ClaroPrimaryContainer,
    onPrimaryContainer = ClaroOnPrimaryContainer,
    secondary = ClaroSecondary,
    onSecondary = ClaroOnSecondary,
    secondaryContainer = ClaroSecondaryContainer,
    onSecondaryContainer = ClaroOnSecondaryContainer,
    tertiary = ClaroTertiary,
    onTertiary = ClaroOnTertiary,
    tertiaryContainer = ClaroTertiaryContainer,
    onTertiaryContainer = ClaroOnTertiaryContainer,
    background = ClaroBackground,
    onBackground = ClaroOnBackground,
    surface = ClaroSurface,
    onSurface = ClaroOnSurface,
    surfaceVariant = ClaroSurfaceVariant,
    onSurfaceVariant = ClaroOnSurfaceVariant,
    outline = ClaroOutline,
    outlineVariant = ClaroOutlineVariant,
    surfaceContainerLowest = ClaroSurfaceContainerLowest,
    surfaceContainerLow = ClaroSurfaceContainerLow,
    surfaceContainer = ClaroSurfaceContainer,
    surfaceContainerHigh = ClaroSurfaceContainerHigh,
    surfaceContainerHighest = ClaroSurfaceContainerHighest,
    error = ClaroError,
    onError = ClaroOnError,
    errorContainer = ClaroErrorContainer,
    onErrorContainer = ClaroOnErrorContainer
)

private val EsquemaOscuro = darkColorScheme(
    primary = OscuroPrimary,
    onPrimary = OscuroOnPrimary,
    primaryContainer = OscuroPrimaryContainer,
    onPrimaryContainer = OscuroOnPrimaryContainer,
    secondary = OscuroSecondary,
    onSecondary = OscuroOnSecondary,
    secondaryContainer = OscuroSecondaryContainer,
    onSecondaryContainer = OscuroOnSecondaryContainer,
    tertiary = OscuroTertiary,
    onTertiary = OscuroOnTertiary,
    tertiaryContainer = OscuroTertiaryContainer,
    onTertiaryContainer = OscuroOnTertiaryContainer,
    background = OscuroBackground,
    onBackground = OscuroOnBackground,
    surface = OscuroSurface,
    onSurface = OscuroOnSurface,
    surfaceVariant = OscuroSurfaceVariant,
    onSurfaceVariant = OscuroOnSurfaceVariant,
    outline = OscuroOutline,
    outlineVariant = OscuroOutlineVariant,
    surfaceContainerLowest = OscuroSurfaceContainerLowest,
    surfaceContainerLow = OscuroSurfaceContainerLow,
    surfaceContainer = OscuroSurfaceContainer,
    surfaceContainerHigh = OscuroSurfaceContainerHigh,
    surfaceContainerHighest = OscuroSurfaceContainerHighest,
    error = OscuroError,
    onError = OscuroOnError,
    errorContainer = OscuroErrorContainer,
    onErrorContainer = OscuroOnErrorContainer
)

@Composable
fun SplitSnapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}