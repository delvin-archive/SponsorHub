package com.example.sponsorhub.core.utils

import android.widget.Toast
import androidx.compose.ui.graphics.Color

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.length >= 6
}

fun Int.toStarColor(): Color {
    return when (this) {
        in 1..2 -> Color.Red
        3 -> Color.Yellow
        else -> Color.Green
    }
}