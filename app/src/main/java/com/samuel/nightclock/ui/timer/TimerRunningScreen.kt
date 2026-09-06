package com.samuel.nightclock.ui.timer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuel.nightclock.NightClockUiColors
import com.samuel.nightclock.ui.layout.NightClockLayoutInfo
import com.samuel.nightclock.ui.layout.NightClockScreenSize

@Composable
fun TimerRunningScreen(
    modifier: Modifier = Modifier,
    layoutInfo: NightClockLayoutInfo,
    timerSeconds: Int,
    totalTimerSeconds: Int,
    currentTimeText: String,
    burnInOffset: IntOffset,
    isTimerRunning: Boolean,
    onTogglePause: () -> Unit,
    appColors: NightClockUiColors,
    onReset: () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val timerScale = when (layoutInfo.screenSize) {
            NightClockScreenSize.COMPACT -> 1f
            NightClockScreenSize.MEDIUM -> 1.15f
            NightClockScreenSize.EXPANDED -> 1.35f
        }

        val ringLimit = when (layoutInfo.screenSize) {
            NightClockScreenSize.COMPACT -> 270.dp
            NightClockScreenSize.MEDIUM -> 340.dp
            NightClockScreenSize.EXPANDED -> 430.dp
        }

        val ringSize = minOf(
            maxHeight * 0.58f,
            ringLimit
        )

        LargeRadialTimer(
            appColors = appColors,
            modifier = Modifier
                .align(Alignment.Center)
                .offset { burnInOffset },
            timerSeconds = timerSeconds,
            totalTimerSeconds = totalTimerSeconds,
            ringSize = ringSize,
            contentScale = timerScale
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 28.dp, end = 36.dp),
            text = currentTimeText,
            color = appColors.secondary,
            fontSize = (18f * timerScale).sp,
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
                    color = appColors.main,
                    fontSize = (14f * timerScale).sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            TextButton(
                onClick = onReset
            ) {
                Text(
                    text = "Reset",
                    color = appColors.secondary,
                    fontSize = (14f * timerScale).sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
private fun LargeRadialTimer(
    modifier: Modifier = Modifier,
    timerSeconds: Int,
    totalTimerSeconds: Int,
    ringSize: Dp,
    contentScale: Float,
    appColors: NightClockUiColors
) {
    val progress = if (totalTimerSeconds > 0) {
        timerSeconds.toFloat() / totalTimerSeconds.toFloat()
    } else {
        0f
    }

    val progressColor = appColors.main
    val mainTextColor = appColors.main
    val labelColor = appColors.secondary
    val trackColor = appColors.track

    Box(
        modifier = modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val strokeWidth =
                (6f * contentScale).dp.toPx()
            val inset = strokeWidth / 2f

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(
                    size.width - strokeWidth,
                    size.height - strokeWidth
                ),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Butt
                )
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(
                    size.width - strokeWidth,
                    size.height - strokeWidth
                ),
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
                color = mainTextColor,
                fontSize = (48f * contentScale).sp,
                fontWeight = FontWeight.ExtraLight,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = "remaining",
                color = labelColor,
                fontSize = (13f * contentScale).sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

private fun formatTimer(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return if (hours > 0) {
        "%02d:%02d:%02d".format(
            hours,
            minutes,
            remainingSeconds
        )
    } else {
        "%02d:%02d".format(
            minutes,
            remainingSeconds
        )
    }
}