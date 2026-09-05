package com.samuel.nightclock.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.nightclock.data.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import com.samuel.nightclock.model.AlarmSound

class NightClockViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val settingsRepository =
        SettingsRepository(application.applicationContext)

    val soundEnabled = settingsRepository.soundEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    val vibrationEnabled = settingsRepository.vibrationEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    val batteryWarningEnabled = settingsRepository.batteryWarningEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    val dimModeEnabled = settingsRepository.dimModeEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val clockStyle = settingsRepository.clockStyle.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0
    )

    val accentColorIndex = settingsRepository.accentColorIndex.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0
    )

    val customTimerMinutes = settingsRepository.customTimerMinutes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 30
    )

    val timerPreset1 = settingsRepository.timerPreset1.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 5
    )

    val timerPreset2 = settingsRepository.timerPreset2.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 15
    )

    val timerPreset3 = settingsRepository.timerPreset3.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 30
    )

    val alarmSound = settingsRepository.alarmSound.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AlarmSound.SOFT
    )

    val alarmVolume = settingsRepository.alarmVolume.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 100
    )

    val gradualAlarmEnabled =
        settingsRepository.gradualAlarmEnabled.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val autoDimEnabled = settingsRepository.autoDimEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val autoDimStartMinutes =
        settingsRepository.autoDimStartMinutes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 22 * 60
        )

    val autoDimEndMinutes =
        settingsRepository.autoDimEndMinutes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 7 * 60
        )

    var timerSeconds by mutableIntStateOf(0)
        private set

    var totalTimerSeconds by mutableIntStateOf(0)
        private set

    var isTimerRunning by mutableStateOf(false)
        private set

    var timerFinished by mutableStateOf(false)
        private set

    private var countdownJob: Job? = null

    fun startTimer(minutes: Int) {
        countdownJob?.cancel()

        timerFinished = false
        timerSeconds = minutes * 60
        totalTimerSeconds = minutes * 60
        isTimerRunning = true

        startCountdown()
    }

    fun togglePause() {
        if (timerSeconds <= 0 || timerFinished) return

        isTimerRunning = !isTimerRunning
    }

    fun resetTimer() {
        countdownJob?.cancel()
        countdownJob = null

        timerFinished = false
        timerSeconds = 0
        totalTimerSeconds = 0
        isTimerRunning = false
    }

    fun dismissFinishedTimer() {
        resetTimer()
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSoundEnabled(enabled)
        }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setVibrationEnabled(enabled)
        }
    }

    fun setBatteryWarningEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setBatteryWarningEnabled(enabled)
        }
    }

    fun setDimModeEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDimModeEnabled(enabled)
        }
    }

    fun setClockStyle(style: Int) {
        viewModelScope.launch {
            settingsRepository.setClockStyle(style)
        }
    }

    fun setAccentColorIndex(index: Int) {
        viewModelScope.launch {
            settingsRepository.setAccentColorIndex(index)
        }
    }

    fun setCustomTimerMinutes(minutes: Int) {
        if (minutes <= 0) return

        viewModelScope.launch {
            settingsRepository.setCustomTimerMinutes(minutes)
        }
    }

    fun setTimerPreset1(minutes: Int) {
        if (minutes <= 0) return

        viewModelScope.launch {
            settingsRepository.setTimerPreset1(minutes)
        }
    }

    fun setTimerPreset2(minutes: Int) {
        if (minutes <= 0) return

        viewModelScope.launch {
            settingsRepository.setTimerPreset2(minutes)
        }
    }

    fun setTimerPreset3(minutes: Int) {
        if (minutes <= 0) return

        viewModelScope.launch {
            settingsRepository.setTimerPreset3(minutes)
        }
    }

    fun setAlarmSound(alarmSound: AlarmSound) {
        viewModelScope.launch {
            settingsRepository.setAlarmSound(alarmSound)
        }
    }

    fun setAlarmVolume(volume: Int) {
        viewModelScope.launch {
            settingsRepository.setAlarmVolume(
                volume.coerceIn(0, 100)
            )
        }
    }

    fun setGradualAlarmEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setGradualAlarmEnabled(enabled)
        }
    }

    fun setAutoDimEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAutoDimEnabled(enabled)
        }
    }

    fun setAutoDimStartMinutes(minutes: Int) {
        viewModelScope.launch {
            settingsRepository.setAutoDimStartMinutes(
                minutes.coerceIn(0, 1439)
            )
        }
    }

    fun setAutoDimEndMinutes(minutes: Int) {
        viewModelScope.launch {
            settingsRepository.setAutoDimEndMinutes(
                minutes.coerceIn(0, 1439)
            )
        }
    }

    private fun startCountdown() {
        countdownJob = viewModelScope.launch {
            while (timerSeconds > 0) {
                delay(1.seconds)

                if (isTimerRunning) {
                    tickTimer()
                }
            }
        }
    }

    private fun tickTimer() {
        if (timerSeconds <= 0) return

        timerSeconds--

        if (timerSeconds <= 0) {
            timerSeconds = 0
            isTimerRunning = false
            timerFinished = true
            countdownJob = null
        }
    }
}