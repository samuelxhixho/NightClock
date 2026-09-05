package com.samuel.nightclock.ui

import android.content.Context
import android.os.PowerManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuel.nightclock.getNightClockUiColors
import com.samuel.nightclock.ui.clock.ClockHomeScreen
import com.samuel.nightclock.ui.settings.SettingsOverlay
import com.samuel.nightclock.ui.components.DismissibleOverlay
import com.samuel.nightclock.ui.timer.QuickTimerControls
import com.samuel.nightclock.ui.timer.TimerDoneScreen
import com.samuel.nightclock.ui.timer.CustomTimerOverlay
import com.samuel.nightclock.ui.timer.TimerRunningScreen
import com.samuel.nightclock.viewmodel.NightClockViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun NightClockApp(
    nightClockViewModel: NightClockViewModel
) {
    val context = LocalContext.current

    val soundEnabled by nightClockViewModel.soundEnabled.collectAsState()
    val vibrationEnabled by nightClockViewModel.vibrationEnabled.collectAsState()
    val dimModeEnabled by nightClockViewModel.dimModeEnabled.collectAsState()

    val batteryWarningEnabled by
    nightClockViewModel.batteryWarningEnabled.collectAsState()

    val clockStyle by nightClockViewModel.clockStyle.collectAsState()

    val accentColorIndex by
    nightClockViewModel.accentColorIndex.collectAsState()

    val customTimerMinutes by
    nightClockViewModel.customTimerMinutes.collectAsState()

    val powerManager = remember {
        context.getSystemService(Context.POWER_SERVICE) as PowerManager
    }

    var isPowerSaveMode by remember {
        mutableStateOf(powerManager.isPowerSaveMode)
    }

    var currentTime by remember {
        mutableStateOf(LocalDateTime.now())
    }

    var burnInOffset by remember {
        mutableStateOf(IntOffset(0, 0))
    }

    var showTimerControls by remember {
        mutableStateOf(false)
    }

    var showSettings by remember {
        mutableStateOf(false)
    }

    var showCustomTimer by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now()
            delay(1000)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000)

            burnInOffset = IntOffset(
                x = Random.nextInt(-8, 9),
                y = Random.nextInt(-8, 9)
            )
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            isPowerSaveMode = powerManager.isPowerSaveMode
            delay(2000)
        }
    }

    val timeText =
        currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    val dateText =
        currentTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))

    val hourText =
        currentTime.format(DateTimeFormatter.ofPattern("HH"))

    val minuteText =
        currentTime.format(DateTimeFormatter.ofPattern("mm"))

    val secondText =
        currentTime.format(DateTimeFormatter.ofPattern("ss"))

    val appColors = getNightClockUiColors(
        accentColorIndex = accentColorIndex,
        dimModeEnabled = dimModeEnabled
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                enabled = !showSettings && !showCustomTimer
            ) {
                if (
                    nightClockViewModel.timerSeconds == 0 &&
                    !nightClockViewModel.timerFinished
                ) {
                    showTimerControls = !showTimerControls
                }
            }
    ) {
        if (batteryWarningEnabled && isPowerSaveMode) {
            PowerSaverWarning(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 28.dp, top = 24.dp)
            )
        }

        if (
            nightClockViewModel.timerSeconds == 0 &&
            !nightClockViewModel.timerFinished &&
            !showCustomTimer
        ) {
            TextButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 18.dp, end = 24.dp),
                onClick = {
                    showSettings = true
                    showTimerControls = false
                }
            ) {
                Text(
                    text = "Settings",
                    color = Color(0xFF666666),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }

        if (nightClockViewModel.timerFinished) {
            TimerDoneScreen(
                modifier = Modifier.fillMaxSize(),
                currentTimeText = timeText,
                context = context,
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled,
                dimModeEnabled = dimModeEnabled,
                appColors = appColors,
                onDismiss = {
                    nightClockViewModel.dismissFinishedTimer()
                    showTimerControls = false
                }
            )
        } else if (nightClockViewModel.timerSeconds > 0) {
            TimerRunningScreen(
                modifier = Modifier.fillMaxSize(),
                timerSeconds = nightClockViewModel.timerSeconds,
                totalTimerSeconds = nightClockViewModel.totalTimerSeconds,
                currentTimeText = timeText,
                burnInOffset = burnInOffset,
                isTimerRunning = nightClockViewModel.isTimerRunning,
                appColors = appColors,
                onTogglePause = {
                    nightClockViewModel.togglePause()
                },
                onReset = {
                    nightClockViewModel.resetTimer()
                    showTimerControls = false
                }
            )
        } else {
            ClockHomeScreen(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = burnInOffset.x.dp,
                        y = if (showTimerControls) {
                            (-44).dp
                        } else {
                            burnInOffset.y.dp
                        }
                    ),
                timeText = timeText,
                hourText = hourText,
                minuteText = minuteText,
                secondText = secondText,
                dateText = dateText,
                dimModeEnabled = dimModeEnabled,
                clockStyle = clockStyle,
                appColors = appColors
            )

            if (showTimerControls) {
                QuickTimerControls(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 28.dp),
                    appColors = appColors,
                    onStartTimer = { minutes ->
                        nightClockViewModel.startTimer(minutes)
                        showTimerControls = false
                    },
                    onCustomTimer = {
                        showTimerControls = false
                        showCustomTimer = true
                    }
                )
            }
        }

        if (showSettings) {
            DismissibleOverlay(
                onDismiss = {
                    showSettings = false
                }
            ) {
                SettingsOverlay(
                    soundEnabled = soundEnabled,
                    vibrationEnabled = vibrationEnabled,
                    batteryWarningEnabled = batteryWarningEnabled,
                    dimModeEnabled = dimModeEnabled,
                    clockStyle = clockStyle,
                    accentColorIndex = accentColorIndex,
                    onAccentColorChange = { index ->
                        nightClockViewModel.setAccentColorIndex(index)
                    },
                    onSoundChange = { enabled ->
                        nightClockViewModel.setSoundEnabled(enabled)
                    },
                    onVibrationChange = { enabled ->
                        nightClockViewModel.setVibrationEnabled(enabled)
                    },
                    onBatteryWarningChange = { enabled ->
                        nightClockViewModel.setBatteryWarningEnabled(enabled)
                    },
                    onDimModeChange = { enabled ->
                        nightClockViewModel.setDimModeEnabled(enabled)
                    },
                    onClose = {
                        showSettings = false
                    },
                    onClockStyleChange = { style ->
                        nightClockViewModel.setClockStyle(style)
                    }
                )
            }
        }

        if (showCustomTimer) {
            DismissibleOverlay(
                onDismiss = {
                    showCustomTimer = false
                }
            ) {
                CustomTimerOverlay(
                    initialMinutes = customTimerMinutes,
                    appColors = appColors,
                    onStart = { minutes ->
                        nightClockViewModel.setCustomTimerMinutes(minutes)
                        nightClockViewModel.startTimer(minutes)
                        showCustomTimer = false
                    },
                    onClose = {
                        showCustomTimer = false
                    }
                )
            }
        }
    }
}

@Composable
private fun PowerSaverWarning(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = "Battery saver is on · vibration may not work",
        color = Color(0xFF777777),
        fontSize = 13.sp,
        fontWeight = FontWeight.Light
    )
}