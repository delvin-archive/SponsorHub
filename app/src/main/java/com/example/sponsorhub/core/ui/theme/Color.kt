package com.example.sponsorhub.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Palet Warna Baru (Lebih Colourful & Modern)
val PrimaryColor = Color(0xFF4F46E5) // Indigo cerah (Profesional & Modern)
val SecondaryColor = Color(0xFFF59E0B) // Amber/Kuning Jeruk (Aksen ceria & narik perhatian)
val BackgroundColor = Color(0xFFF1F5F9) // Abu-abu super muda (Biar mata gak capek)
val SurfaceColor = Color(0xFFFFFFFF) // Putih bersih buat Card/Background Menu

val TextPrimary = Color(0xFF0F172A) // Hampir hitam (Kontras tinggi, gampang dibaca)
val TextSecondary = Color(0xFF64748B) // Abu-abu gelap (Buat teks tambahan)

val SuccessColor = Color(0xFF10B981) // Hijau fresh
val ErrorColor = Color(0xFFEF4444) // Merah tegas

private val SponsorHubColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    error = ErrorColor,
    onPrimary = Color.White, // Teks di atas warna utama selalu putih
    onSurface = TextPrimary // Teks di atas warna putih jadi gelap
)

