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
import com.samuel.nightclock.NightClockUiColors
import com.samuel.nightclock.util.playTimerFinishedSound
import com.samuel.nightclock.util.vibrateTimerFinished

@Composable
fun TimerDoneScreen(
    modifier: Modifier = Modifier,
    currentTimeText: String,
    context: Context,
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    dimModeEnabled: Boolean,
    appColors: NightClockUiColors,
    onDismiss: () -> Unit
) {
    LaunchedEffect(Unit) {
        if (vibrationEnabled) {
            vibrateTimerFinished(context)
        }

        if (soundEnabled) {
            playTimerFinishedSound()
        }
    }

    val doneTextColor =
        if (dimModeEnabled) Color(0xFF8A8A8A) else Color.White

    val timeColor =
        if (dimModeEnabled) Color(0xFF4A4A4A) else Color(0xFF666666)

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
                color = appColors.secondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}