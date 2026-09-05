package com.samuel.nightclock.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val context: Context
) {

    val soundEnabled = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.SOUND_ENABLED] ?: true
    }

    val vibrationEnabled = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.VIBRATION_ENABLED] ?: true
    }

    val batteryWarningEnabled = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.BATTERY_WARNING_ENABLED] ?: true
    }

    val dimModeEnabled = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.DIM_MODE_ENABLED] ?: false
    }

    val clockStyle = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.CLOCK_STYLE] ?: 0
    }

    val accentColorIndex = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.ACCENT_COLOR] ?: 0
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.VIBRATION_ENABLED] = enabled
        }
    }

    suspend fun setBatteryWarningEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.BATTERY_WARNING_ENABLED] = enabled
        }
    }

    suspend fun setDimModeEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.DIM_MODE_ENABLED] = enabled
        }
    }

    suspend fun setClockStyle(style: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.CLOCK_STYLE] = style
        }
    }

    suspend fun setAccentColorIndex(index: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.ACCENT_COLOR] = index
        }
    }
}