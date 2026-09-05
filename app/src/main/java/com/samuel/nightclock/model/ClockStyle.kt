package com.samuel.nightclock.model

enum class ClockStyle(
    val storageValue: Int,
    val displayName: String
) {
    CLASSIC(
        storageValue = 0,
        displayName = "Classic"
    ),

    STACKED(
        storageValue = 1,
        displayName = "Stacked"
    ),

    MINIMAL(
        storageValue = 2,
        displayName = "Minimal"
    ),

    SPLIT(
        storageValue = 3,
        displayName = "Split"
    ),

    SECONDS(
        storageValue = 4,
        displayName = "Seconds"
    ),

    ANALOG(
        storageValue = 5,
        displayName = "Analog"
    ),


    FOCUS(
    storageValue = 6,
    displayName = "Focus"
    ),

    WIDE(
    storageValue = 7,
    displayName = "Wide"
    );

    companion object {
        fun fromStorageValue(value: Int): ClockStyle {
            return entries.firstOrNull {
                it.storageValue == value
            } ?: CLASSIC
        }
    }
}