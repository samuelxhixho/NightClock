package com.samuel.nightclock.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.samuel.nightclock.model.AlarmSound
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

    val customTimerMinutes = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.CUSTOM_TIMER_MINUTES] ?: 30
    }

    val timerPreset1 = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.TIMER_PRESET_1] ?: 5
    }

    val timerPreset2 = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.TIMER_PRESET_2] ?: 15
    }

    val timerPreset3 = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.TIMER_PRESET_3] ?: 30
    }

    val alarmSound = context.settingsDataStore.data.map { preferences ->
        AlarmSound.fromStorageValue(
            preferences[SettingsKeys.ALARM_SOUND]
        )
    }

    val alarmVolume = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.ALARM_VOLUME] ?: 100
    }

    val gradualAlarmEnabled = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.GRADUAL_ALARM] ?: false
    }

    val autoDimEnabled = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.AUTO_DIM_ENABLED] ?: false
    }

    val autoDimStartMinutes = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.AUTO_DIM_START_MINUTES] ?: (22 * 60)
    }

    val autoDimEndMinutes = context.settingsDataStore.data.map { preferences ->
        preferences[SettingsKeys.AUTO_DIM_END_MINUTES] ?: (7 * 60)
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

    suspend fun setCustomTimerMinutes(minutes: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.CUSTOM_TIMER_MINUTES] = minutes
        }
    }

    suspend fun setTimerPreset1(minutes: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.TIMER_PRESET_1] = minutes
        }
    }

    suspend fun setTimerPreset2(minutes: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.TIMER_PRESET_2] = minutes
        }
    }

    suspend fun setTimerPreset3(minutes: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.TIMER_PRESET_3] = minutes
        }
    }

    suspend fun setAlarmSound(alarmSound: AlarmSound) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.ALARM_SOUND] =
                alarmSound.storageValue
        }
    }

    suspend fun setAlarmVolume(volume: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.ALARM_VOLUME] =
                volume.coerceIn(0, 100)
        }
    }

    suspend fun setGradualAlarmEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.GRADUAL_ALARM] = enabled
        }
    }

    suspend fun setAutoDimEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.AUTO_DIM_ENABLED] = enabled
        }
    }

    suspend fun setAutoDimStartMinutes(minutes: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.AUTO_DIM_START_MINUTES] =
                minutes.coerceIn(0, 1439)
        }
    }

    suspend fun setAutoDimEndMinutes(minutes: Int) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.AUTO_DIM_END_MINUTES] =
                minutes.coerceIn(0, 1439)
        }
    }
}