package com.samuel.nightclock.model

enum class AlarmSound(
    val storageValue: String,
    val displayName: String
) {
    SOFT(
        storageValue = "soft",
        displayName = "Soft"
    ),
    DIGITAL(
        storageValue = "digital",
        displayName = "Digital"
    ),
    BELL(
        storageValue = "bell",
        displayName = "Bell"
    ),
    PULSE(
        storageValue = "pulse",
        displayName = "Pulse"
    );

    companion object {
        fun fromStorageValue(value: String?): AlarmSound {
            return entries.firstOrNull {
                it.storageValue == value
            } ?: SOFT
        }
    }
}