package com.samuel.nightclock

import androidx.compose.ui.graphics.Color

data class NightClockUiColors(
    val main: Color,
    val secondary: Color,
    val muted: Color,
    val track: Color,
    val buttonBackground: Color,
    val buttonBorder: Color
)

fun getNightClockUiColors(
    accentColorIndex: Int,
    dimModeEnabled: Boolean
): NightClockUiColors {
    val accent = when (accentColorIndex) {
        1 -> Color(0xFFFF5A5F)
        2 -> Color(0xFF64B5F6)
        3 -> Color(0xFF66BB6A)
        4 -> Color(0xFFB388FF)
        5 -> Color(0xFFFFC857)
        else -> Color.White
    }

    return NightClockUiColors(
        main = if (dimModeEnabled) {
            accent.copy(alpha = 0.55f)
        } else {
            accent
        },
        secondary = if (dimModeEnabled) {
            accent.copy(alpha = 0.32f)
        } else {
            accent.copy(alpha = 0.55f)
        },
        muted = if (dimModeEnabled) {
            accent.copy(alpha = 0.22f)
        } else {
            accent.copy(alpha = 0.38f)
        },
        track = if (dimModeEnabled) {
            accent.copy(alpha = 0.08f)
        } else {
            accent.copy(alpha = 0.14f)
        },
        buttonBackground = Color(0xFF090909),
        buttonBorder = if (dimModeEnabled) {
            accent.copy(alpha = 0.25f)
        } else {
            accent.copy(alpha = 0.40f)
        }
    )
}