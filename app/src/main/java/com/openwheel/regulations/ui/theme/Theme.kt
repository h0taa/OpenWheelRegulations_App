package com.openwheel.regulations.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val OwrDarkColors = darkColorScheme(
    primary = AccentRed,
    onPrimary = OffWhite,
    primaryContainer = Crimson,
    onPrimaryContainer = OffWhite,
    secondary = Crimson,
    onSecondary = OffWhite,
    background = DarkBg,
    onBackground = OffWhite,
    surface = DarkSurface,
    onSurface = OffWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceMuted,
    outline = DarkOnSurfaceMuted,
    error = AccentRed,
    onError = OffWhite
)

private val OwrLightColors = lightColorScheme(
    primary = Crimson,
    onPrimary = OffWhite,
    primaryContainer = AccentRed,
    onPrimaryContainer = OffWhite,
    secondary = Navy,
    onSecondary = OffWhite,
    background = LightBg,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceMuted,
    outline = LightOnSurfaceMuted,
    error = Crimson,
    onError = OffWhite
)

/** Dynamic color is only available on Android 12 (API 31)+. */
val supportsDynamicColor: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
fun OWRTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && supportsDynamicColor -> {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> OwrDarkColors
        else -> OwrLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
