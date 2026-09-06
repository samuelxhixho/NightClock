package com.samuel.nightclock.ui.timer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuel.nightclock.NightClockUiColors
import com.samuel.nightclock.ui.layout.NightClockLayoutInfo
import com.samuel.nightclock.ui.layout.NightClockScreenSize

@Composable
fun QuickTimerControls(
    modifier: Modifier = Modifier,
    layoutInfo: NightClockLayoutInfo,
    preset1Minutes: Int,
    preset2Minutes: Int,
    preset3Minutes: Int,
    appColors: NightClockUiColors,
    onStartTimer: (Int) -> Unit,
    onCustomTimer: () -> Unit
){

    val controlScale = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 1f
        NightClockScreenSize.MEDIUM -> 1.15f
        NightClockScreenSize.EXPANDED -> 1.3f
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimerButton(
            text = formatPresetLabel(preset1Minutes),
            appColors = appColors,
            contentScale = controlScale,
            onClick = {
                onStartTimer(preset1Minutes)
            }
        )

        Spacer(
            modifier = Modifier.width(
                (12f * controlScale).dp
            )
        )

        TimerButton(
            text = formatPresetLabel(preset2Minutes),
            appColors = appColors,
            contentScale = controlScale,
            onClick = {
                onStartTimer(preset2Minutes)
            }
        )

        Spacer(
            modifier = Modifier.width(
                (12f * controlScale).dp
            )
        )

        TimerButton(
            text = formatPresetLabel(preset3Minutes),
            appColors = appColors,
            contentScale = controlScale,
            onClick = {
                onStartTimer(preset3Minutes)
            }
        )

        Spacer(
            modifier = Modifier.width(
                (12f * controlScale).dp
            )
        )

        TimerButton(
            text = "Custom",
            appColors = appColors,
            contentScale = controlScale,
            onClick = onCustomTimer
        )
    }
}

@Composable
private fun TimerButton(
    text: String,
    appColors: NightClockUiColors,
    contentScale: Float,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(100.dp),
        border = BorderStroke(
            width = 1.dp,
            color = appColors.buttonBorder
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors.buttonBackground,
            contentColor = appColors.main
        ),
        contentPadding = PaddingValues(
            horizontal = (18f * contentScale).dp,
            vertical = (7f * contentScale).dp
        )
    ) {
        Text(
            text = text,
            fontSize = (13f * contentScale).sp,
            fontWeight = FontWeight.Light
        )
    }
}

private fun formatPresetLabel(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return when {
        hours == 0 -> "$minutes min"
        minutes == 0 -> "${hours}h"
        else -> "${hours}h ${minutes}m"
    }
}