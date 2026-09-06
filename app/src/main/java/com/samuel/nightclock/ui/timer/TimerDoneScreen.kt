package com.samuel.nightclock.ui.timer

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.DisposableEffect
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.IntOffset
import com.samuel.nightclock.NightClockUiColors
import com.samuel.nightclock.util.playTimerFinishedSound
import com.samuel.nightclock.util.vibrateTimerFinished
import com.samuel.nightclock.model.AlarmSound
import com.samuel.nightclock.util.stopTimerFinishedSound
import com.samuel.nightclock.ui.layout.NightClockLayoutInfo
import com.samuel.nightclock.ui.layout.NightClockScreenSize

@Composable
fun TimerDoneScreen(
    modifier: Modifier = Modifier,
    layoutInfo: NightClockLayoutInfo,
    burnInOffset: IntOffset,
    currentTimeText: String,
    context: Context,
    soundEnabled: Boolean,
    alarmSound: AlarmSound,
    alarmVolume: Int,
    gradualAlarmEnabled: Boolean,
    vibrationEnabled: Boolean,
    dimModeEnabled: Boolean,
    appColors: NightClockUiColors,
    onAddFiveMinutes: () -> Unit,
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        if (vibrationEnabled) {
            vibrateTimerFinished(context)
        }

        if (soundEnabled) {
            playTimerFinishedSound(
                context = context,
                alarmSound = alarmSound,
                volumePercent = alarmVolume,
                gradualAlarmEnabled = gradualAlarmEnabled,
                loop = true
            )
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            stopTimerFinishedSound()
        }
    }

    val doneTextColor =
        if (dimModeEnabled) Color(0xFF8A8A8A) else Color.White

    val timeColor =
        if (dimModeEnabled) Color(0xFF4A4A4A) else Color(0xFF666666)

    val doneScale = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 1f
        NightClockScreenSize.MEDIUM -> 1.15f
        NightClockScreenSize.EXPANDED -> 1.35f
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset { burnInOffset },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Done",
                color = doneTextColor,
                fontSize = (72f * doneScale).sp,
                fontWeight = FontWeight.ExtraLight,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentTimeText,
                color = timeColor,
                fontSize = (22f * doneScale).sp,
                fontWeight = FontWeight.Light
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 34.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onAddFiveMinutes
            ) {
                Text(
                    text = "+5 min",
                    color = appColors.main,
                    fontSize = (14f * doneScale).sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(
                modifier = Modifier.width(
                    (12f * doneScale).dp
                )
            )

            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Dismiss",
                    color = appColors.secondary,
                    fontSize = (14f * doneScale).sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}