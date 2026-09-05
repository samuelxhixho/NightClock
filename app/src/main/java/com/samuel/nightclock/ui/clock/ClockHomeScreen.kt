package com.samuel.nightclock.ui.clock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuel.nightclock.NightClockUiColors

@Composable
fun ClockHomeScreen(
    modifier: Modifier = Modifier,
    timeText: String,
    hourText: String,
    minuteText: String,
    secondText: String,
    dateText: String,
    dimModeEnabled: Boolean,
    clockStyle: Int,
    appColors: NightClockUiColors
) {
    val mainTextColor = appColors.main
    val secondaryTextColor = appColors.secondary

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (clockStyle) {
            0 -> {
                Text(
                    text = timeText,
                    color = mainTextColor,
                    fontSize = 104.sp,
                    fontWeight = FontWeight.ExtraLight,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 2.sp
                )
            }

            1 -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = hourText,
                        color = mainTextColor,
                        fontSize = 82.sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = minuteText,
                        color = mainTextColor,
                        fontSize = 82.sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 2.sp
                    )
                }
            }

            2 -> {
                Text(
                    text = "$hourText · $minuteText",
                    color = mainTextColor,
                    fontSize = 96.sp,
                    fontWeight = FontWeight.ExtraLight,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 4.sp
                )
            }

            3 -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = hourText,
                        color = mainTextColor,
                        fontSize = 96.sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = FontFamily.SansSerif
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = "|",
                        color = secondaryTextColor,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.ExtraLight
                    )

                    Text(
                        text = minuteText,
                        color = mainTextColor,
                        fontSize = 96.sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            4 -> {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = timeText,
                        color = mainTextColor,
                        fontSize = 96.sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 2.sp
                    )

                    Text(
                        modifier = Modifier.padding(
                            start = 12.dp,
                            bottom = 12.dp
                        ),
                        text = secondText,
                        color = secondaryTextColor,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = dateText,
            color = secondaryTextColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light
        )
    }
}