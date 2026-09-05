package com.samuel.nightclock.model

enum class AccentColorPreset(
    val storageValue: Int,
    val displayName: String
) {
    WHITE(
        storageValue = 0,
        displayName = "White"
    ),

    RED(
        storageValue = 1,
        displayName = "Red"
    ),

    BLUE(
        storageValue = 2,
        displayName = "Blue"
    ),

    GREEN(
        storageValue = 3,
        displayName = "Green"
    ),

    PURPLE(
        storageValue = 4,
        displayName = "Purple"
    ),

    AMBER(
        storageValue = 5,
        displayName = "Amber"
    ),

    CYAN(
        storageValue = 6,
        displayName = "Cyan"
    ),

    PINK(
        storageValue = 7,
        displayName = "Pink"
    ),

    ORANGE(
        storageValue = 8,
        displayName = "Orange"
    ),

    CUSTOM(
        storageValue = 9,
        displayName = "Custom"
    );

    companion object {
        fun fromStorageValue(value: Int): AccentColorPreset {
            return entries.firstOrNull {
                it.storageValue == value
            } ?: WHITE
        }
    }
}