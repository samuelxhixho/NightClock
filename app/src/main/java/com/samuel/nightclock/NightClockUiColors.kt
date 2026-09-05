package com.samuel.nightclock

import androidx.compose.ui.graphics.Color
import com.samuel.nightclock.model.AccentColorPreset

data class NightClockUiColors(
    val main: Color,
    val secondary: Color,
    val muted: Color,
    val track: Color,
    val buttonBackground: Color,
    val buttonBorder: Color
)

fun getNightClockUiColors(
    accentColorPreset: AccentColorPreset,
    customAccentColor: Long,
    dimModeEnabled: Boolean
): NightClockUiColors {
    val accentColor = when (accentColorPreset) {
        AccentColorPreset.WHITE -> Color.White
        AccentColorPreset.RED -> Color(0xFFFF5A5F)
        AccentColorPreset.BLUE -> Color(0xFF64B5F6)
        AccentColorPreset.GREEN -> Color(0xFF66BB6A)
        AccentColorPreset.PURPLE -> Color(0xFFB388FF)
        AccentColorPreset.AMBER -> Color(0xFFFFC857)
        AccentColorPreset.CYAN -> Color(0xFF4DD0E1)
        AccentColorPreset.PINK -> Color(0xFFFF80AB)
        AccentColorPreset.ORANGE -> Color(0xFFFF9F43)

        AccentColorPreset.CUSTOM ->
            Color(customAccentColor)
    }

    return NightClockUiColors(
        main = if (dimModeEnabled) {
            accentColor.copy(alpha = 0.55f)
        } else {
            accentColor
        },
        secondary = if (dimModeEnabled) {
            accentColor.copy(alpha = 0.32f)
        } else {
            accentColor.copy(alpha = 0.55f)
        },
        muted = if (dimModeEnabled) {
            accentColor.copy(alpha = 0.22f)
        } else {
            accentColor.copy(alpha = 0.38f)
        },
        track = if (dimModeEnabled) {
            accentColor.copy(alpha = 0.08f)
        } else {
            accentColor.copy(alpha = 0.14f)
        },
        buttonBackground = Color(0xFF090909),
        buttonBorder = if (dimModeEnabled) {
            accentColor.copy(alpha = 0.25f)
        } else {
            accentColor.copy(alpha = 0.40f)
        }
    )
}