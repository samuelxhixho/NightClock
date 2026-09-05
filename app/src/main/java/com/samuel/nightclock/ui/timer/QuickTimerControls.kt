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

@Composable
fun QuickTimerControls(
    modifier: Modifier = Modifier,
    appColors: NightClockUiColors,
    onStartTimer: (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimerButton(
            text = "5 min",
            appColors = appColors,
            onClick = { onStartTimer(5) }
        )

        Spacer(modifier = Modifier.width(12.dp))

        TimerButton(
            text = "15 min",
            appColors = appColors,
            onClick = { onStartTimer(15) }
        )

        Spacer(modifier = Modifier.width(12.dp))

        TimerButton(
            text = "30 min",
            appColors = appColors,
            onClick = { onStartTimer(30) }
        )
    }
}

@Composable
private fun TimerButton(
    text: String,
    appColors: NightClockUiColors,
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