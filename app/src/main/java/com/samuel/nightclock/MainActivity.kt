package com.samuel.nightclock

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch



val Context.settingsDataStore by preferencesDataStore(name = "night_clock_settings")

object SettingsKeys {
    val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
    val BATTERY_WARNING_ENABLED = booleanPreferencesKey("battery_warning_enabled")

    val DIM_MODE_ENABLED = booleanPreferencesKey("dim_mode_enabled")
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        setContent {
            NightClockApp()
        }
    }
}

@Composable
fun NightClockApp() {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val soundEnabled by context.settingsDataStore.data
        .map { preferences ->
            preferences[SettingsKeys.SOUND_ENABLED] ?: true
        }
        .collectAsState(initial = true)

    val vibrationEnabled by context.settingsDataStore.data
        .map { preferences ->
            preferences[SettingsKeys.VIBRATION_ENABLED] ?: true
        }
        .collectAsState(initial = true)

    val dimModeEnabled by context.settingsDataStore.data
        .map { preferences ->
            preferences[SettingsKeys.DIM_MODE_ENABLED] ?: false
        }
        .collectAsState(initial = false)

    val batteryWarningEnabled by context.settingsDataStore.data
        .map { preferences ->
            preferences[SettingsKeys.BATTERY_WARNING_ENABLED] ?: true
        }
        .collectAsState(initial = true)

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

    var timerSeconds by remember {
        mutableIntStateOf(0)
    }

    var totalTimerSeconds by remember {
        mutableIntStateOf(0)
    }

    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    var showTimerControls by remember {
        mutableStateOf(false)
    }

    var showSettings by remember {
        mutableStateOf(false)
    }

    var timerFinished by remember {
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

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning && timerSeconds > 0) {
            delay(1000)
            timerSeconds--

            if (timerSeconds <= 0) {
                isTimerRunning = false
                timerFinished = true
            }
        }
    }

    val timeText = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    val dateText = currentTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(enabled = !showSettings) {
                if (timerSeconds == 0 && !timerFinished) {
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

        if (timerSeconds == 0 && !timerFinished) {
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

        if (timerFinished) {
            TimerDoneScreen(
                modifier = Modifier.fillMaxSize(),
                currentTimeText = timeText,
                context = context,
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled,
                dimModeEnabled = dimModeEnabled,
                onDismiss = {
                    timerFinished = false
                    timerSeconds = 0
                    totalTimerSeconds = 0
                    isTimerRunning = false
                    showTimerControls = false
                }
            )
        } else if (timerSeconds > 0) {
            TimerRunningScreen(
                modifier = Modifier.fillMaxSize(),
                timerSeconds = timerSeconds,
                totalTimerSeconds = totalTimerSeconds,
                currentTimeText = timeText,
                burnInOffset = burnInOffset,
                isTimerRunning = isTimerRunning,
                dimModeEnabled = dimModeEnabled,
                onTogglePause = {
                    isTimerRunning = !isTimerRunning
                },
                onReset = {
                    timerFinished = false
                    timerSeconds = 0
                    totalTimerSeconds = 0
                    isTimerRunning = false
                    showTimerControls = false
                }
            )
        } else {
            ClockHomeScreen(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = burnInOffset.x.dp,
                        y = if (showTimerControls) (-44).dp else burnInOffset.y.dp
                    ),
                timeText = timeText,
                dateText = dateText,
                dimModeEnabled = dimModeEnabled
            )

            if (showTimerControls) {
                QuickTimerControls(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 28.dp),
                    onStartTimer = { minutes ->
                        timerFinished = false
                        timerSeconds = minutes * 60
                        totalTimerSeconds = minutes * 60
                        isTimerRunning = true
                        showTimerControls = false
                    }
                )
            }
        }

        if (showSettings) {
            SettingsOverlay(
                modifier = Modifier.align(Alignment.Center),
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled,
                batteryWarningEnabled = batteryWarningEnabled,
                dimModeEnabled = dimModeEnabled,
                onSoundChange = { enabled ->
                    scope.launch {
                        context.settingsDataStore.edit { preferences ->
                            preferences[SettingsKeys.SOUND_ENABLED] = enabled
                        }
                    }
                },
                onVibrationChange = { enabled ->
                    scope.launch {
                        context.settingsDataStore.edit { preferences ->
                            preferences[SettingsKeys.VIBRATION_ENABLED] = enabled
                        }
                    }
                },
                onBatteryWarningChange = { enabled ->
                    scope.launch {
                        context.settingsDataStore.edit { preferences ->
                            preferences[SettingsKeys.BATTERY_WARNING_ENABLED] = enabled
                        }
                    }
                },
                onDimModeChange = { enabled ->
                    scope.launch {
                        context.settingsDataStore.edit { preferences ->
                            preferences[SettingsKeys.DIM_MODE_ENABLED] = enabled
                        }
                    }
                },
                onClose = {
                    showSettings = false
                }
            )
        }
    }
}


@Composable
fun ClockHomeScreen(
    modifier: Modifier = Modifier,
    timeText: String,
    dateText: String,
    dimModeEnabled: Boolean
) {
    val mainTextColor = if (dimModeEnabled) Color(0xFF8A8A8A) else Color.White
    val dateColor = if (dimModeEnabled) Color(0xFF4A4A4A) else Color(0xFF6F6F6F)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = timeText,
            color = mainTextColor,
            fontSize = 104.sp,
            fontWeight = FontWeight.ExtraLight,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 2.sp
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = dateText,
            color = dateColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun TimerRunningScreen(
    modifier: Modifier = Modifier,
    timerSeconds: Int,
    totalTimerSeconds: Int,
    currentTimeText: String,
    burnInOffset: IntOffset,
    isTimerRunning: Boolean,
    dimModeEnabled: Boolean,
    onTogglePause: () -> Unit,
    onReset: () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val ringSize = minOf(maxHeight * 0.58f, 270.dp)

        LargeRadialTimer(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { burnInOffset },
            timerSeconds = timerSeconds,
            totalTimerSeconds = totalTimerSeconds,
            ringSize = ringSize,
            dimModeEnabled = dimModeEnabled
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 28.dp, end = 36.dp),
            text = currentTimeText,
            color = if (dimModeEnabled) Color(0xFF3F3F3F) else Color(0xFF4F4F4F),
            fontSize = 18.sp,
            fontWeight = FontWeight.Light
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 34.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onTogglePause
            ) {
                Text(
                    text = if (isTimerRunning) "Pause" else "Resume",
                    color = Color(0xFFBDBDBD),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            TextButton(
                onClick = onReset
            ) {
                Text(
                    text = "Reset",
                    color = Color(0xFF666666),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}


@Composable
fun TimerDoneScreen(
    modifier: Modifier = Modifier,
    currentTimeText: String,
    context: Context,
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    dimModeEnabled: Boolean,
    onDismiss: () -> Unit
){
    LaunchedEffect(Unit) {
        if (vibrationEnabled) {
            vibrateTimerFinished(context)
        }

        if (soundEnabled) {
            playTimerFinishedSound()
        }
    }

    val doneTextColor = if (dimModeEnabled) Color(0xFF8A8A8A) else Color.White
    val timeColor = if (dimModeEnabled) Color(0xFF4A4A4A) else Color(0xFF666666)

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Done",
                color = doneTextColor,
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraLight,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentTimeText,
                color = timeColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Light
            )
        }

        TextButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 34.dp, bottom = 24.dp),
            onClick = onDismiss
        ) {
            Text(
                text = "Dismiss",
                color = Color(0xFF777777),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun QuickTimerControls(
    modifier: Modifier = Modifier,
    onStartTimer: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimerButton("5 min", onClick = { onStartTimer(5) })

        Spacer(modifier = Modifier.width(12.dp))

        TimerButton("15 min", onClick = { onStartTimer(15) })

        Spacer(modifier = Modifier.width(12.dp))

        TimerButton("30 min", onClick = { onStartTimer(30) })
    }
}

@Composable
fun LargeRadialTimer(
    modifier: Modifier = Modifier,
    timerSeconds: Int,
    totalTimerSeconds: Int,
    ringSize: Dp,
    dimModeEnabled: Boolean
) {
    val progress = if (totalTimerSeconds > 0) {
        timerSeconds.toFloat() / totalTimerSeconds.toFloat()
    } else {
        0f
    }

    val progressColor = if (dimModeEnabled) Color(0xFF8A8A8A) else Color(0xFFEDEDED)
    val mainTextColor = if (dimModeEnabled) Color(0xFF8A8A8A) else Color.White
    val labelColor = if (dimModeEnabled) Color(0xFF454545) else Color(0xFF555555)


    Box(
        modifier = modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val strokeWidth = 6.dp.toPx()
            val inset = strokeWidth / 2f

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Butt
                )
            )

            drawArc(
                color = mainTextColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Butt
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatTimer(timerSeconds),
                color = labelColor,
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraLight,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "remaining",
                color = Color(0xFF555555),
                fontSize = 13.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun TimerButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(100.dp),
        border = BorderStroke(1.dp, Color(0xFF242424)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF090909),
            contentColor = Color(0xFFDADADA)
        ),
        contentPadding = PaddingValues(
            horizontal = 18.dp,
            vertical = 7.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun PowerSaverWarning(
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

@Composable
fun SettingsOverlay(
    modifier: Modifier = Modifier,
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    batteryWarningEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    onVibrationChange: (Boolean) -> Unit,
    onBatteryWarningChange: (Boolean) -> Unit,
    dimModeEnabled: Boolean,
    onDimModeChange: (Boolean) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = modifier
            .width(390.dp)
            .background(
                color = Color(0xFF080808),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraLight
            )

            TextButton(
                onClick = onClose,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "✕",
                    color = Color(0xFFBDBDBD),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        SettingsRow(
            title = "Dim mode",
            subtitle = "Softer colors for night use",
            checked = dimModeEnabled,
            onCheckedChange = onDimModeChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsRow(
            title = "Sound",
            subtitle = "Soft alarm when timer ends",
            checked = soundEnabled,
            onCheckedChange = onSoundChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsRow(
            title = "Vibration",
            subtitle = "Vibrate when timer ends",
            checked = vibrationEnabled,
            onCheckedChange = onVibrationChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsRow(
            title = "Battery warning",
            subtitle = "Warn when battery saver is on",
            checked = batteryWarningEnabled,
            onCheckedChange = onBatteryWarningChange
        )
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )

            Text(
                modifier = Modifier.padding(top = 3.dp),
                text = subtitle,
                color = Color(0xFF666666),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

fun formatTimer(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60

    return "%02d:%02d".format(minutes, remainingSeconds)
}

fun vibrateTimerFinished(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        manager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (!vibrator.hasVibrator()) return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(0, 600, 200, 600, 200, 900),
                intArrayOf(0, 255, 0, 255, 0, 255),
                -1
            )
        )
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(
            longArrayOf(0, 600, 200, 600, 200, 900),
            -1
        )
    }
}

fun playTimerFinishedSound() {
    val toneGenerator = ToneGenerator(
        AudioManager.STREAM_ALARM,
        80
    )

    toneGenerator.startTone(
        ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,
        1200
    )

    Handler(Looper.getMainLooper()).postDelayed({
        toneGenerator.release()
    }, 1400)
}