package com.example.vetapp_usuario.ui.theme
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = IconBackground,
    onPrimaryContainer = PrimaryBlueDark,

    secondary = AccentPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF3E5F5),
    onSecondaryContainer = Color(0xFF4A148C),

    tertiary = AccentGreen,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1FAE5),
    onTertiaryContainer = Color(0xFF065F46),

    error = AccentRed,
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF991B1B),

    background = BackgroundLight,
    onBackground = TextPrimary,

    surface = BackgroundWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundGray,
    onSurfaceVariant = TextSecondary,

    outline = BorderLight,
    outlineVariant = BorderMedium,

    scrim = OverlayDark,

    inverseSurface = Color(0xFF1E293B),
    inverseOnSurface = Color.White,
    inversePrimary = PrimaryBlueLight
)

@Composable
fun VetAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivar dynamic color para mantener el diseño consistente
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Solo tema claro por ahora

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = BackgroundWhite.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}