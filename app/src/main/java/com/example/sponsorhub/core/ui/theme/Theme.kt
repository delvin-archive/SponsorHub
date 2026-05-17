package com.example.sponsorhub.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SponsorHubColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    error = ErrorColor
)

@Composable
fun SponsorHubTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SponsorHubColorScheme,
        typography = Typography,
        content = content
    )
}