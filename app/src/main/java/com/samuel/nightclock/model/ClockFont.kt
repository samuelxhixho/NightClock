package com.samuel.nightclock.model

enum class ClockFont(
    val storageValue: String,
    val displayName: String
) {
    SANS(
        storageValue = "sans",
        displayName = "Sans"
    ),

    SERIF(
        storageValue = "serif",
        displayName = "Serif"
    ),

    MONOSPACE(
        storageValue = "monospace",
        displayName = "Mono"
    ),

    CURSIVE(
        storageValue = "cursive",
        displayName = "Cursive"
    );

    companion object {
        fun fromStorageValue(value: String?): ClockFont {
            return entries.firstOrNull {
                it.storageValue == value
            } ?: SANS
        }
    }
}