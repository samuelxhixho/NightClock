package com.samuel.nightclock.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey

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

    val TIMER_PRESET_1 = intPreferencesKey("timer_preset_1")
    val TIMER_PRESET_2 = intPreferencesKey("timer_preset_2")
    val TIMER_PRESET_3 = intPreferencesKey("timer_preset_3")

    val ALARM_SOUND = stringPreferencesKey("alarm_sound")

}

