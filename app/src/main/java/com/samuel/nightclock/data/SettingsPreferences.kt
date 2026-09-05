package com.samuel.nightclock.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.settingsDataStore by preferencesDataStore(
    name = "night_clock_settings"
)

object SettingsKeys {
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
    val BATTERY_WARNING_ENABLED = booleanPreferencesKey("battery_warning_enabled")
    val DIM_MODE_ENABLED = booleanPreferencesKey("dim_mode_enabled")
    val CLOCK_STYLE = intPreferencesKey("clock_style")
    val ACCENT_COLOR = intPreferencesKey("accent_color")

    val CUSTOM_TIMER_MINUTES = intPreferencesKey("custom_timer_minutes")
}