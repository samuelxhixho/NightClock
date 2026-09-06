package com.samuel.nightclock.ui.clock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.width
import com.samuel.nightclock.NightClockUiColors
import com.samuel.nightclock.model.ClockStyle
import com.samuel.nightclock.model.ClockFont
import com.samuel.nightclock.ui.layout.NightClockLayoutInfo
import com.samuel.nightclock.ui.layout.NightClockScreenSize

@Composable
fun ClockHomeScreen(
    modifier: Modifier = Modifier,
    timeText: String,
    hourText: String,
    minuteText: String,
    secondText: String,
    hourValue: Int,
    minuteValue: Int,
    secondValue: Int,
    dateText: String,
    clockStyle: ClockStyle,
    clockFont: ClockFont,
    appColors: NightClockUiColors,
    layoutInfo: NightClockLayoutInfo
) {
    val mainTextColor = appColors.main
    val secondaryTextColor = appColors.secondary

    val clockScale = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 1f
        NightClockScreenSize.MEDIUM -> 1.15f
        NightClockScreenSize.EXPANDED -> 1.35f
    }

    val dateFontSize = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 20.sp
        NightClockScreenSize.MEDIUM -> 22.sp
        NightClockScreenSize.EXPANDED -> 26.sp
    }

    val analogClockSize = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 220.dp
        NightClockScreenSize.MEDIUM -> 280.dp
        NightClockScreenSize.EXPANDED -> 360.dp
    }

    val selectedFontFamily = when (clockFont) {
        ClockFont.SANS -> FontFamily.SansSerif
        ClockFont.SERIF -> FontFamily.Serif
        ClockFont.MONOSPACE -> FontFamily.Monospace
        ClockFont.CURSIVE -> FontFamily.Cursive
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (clockStyle) {
            ClockStyle.CLASSIC -> {
                Text(
                    text = timeText,
                    color = mainTextColor,
                    fontSize = (104f * clockScale).sp,
                    fontWeight = FontWeight.ExtraLight,
                    fontFamily = selectedFontFamily,
                    letterSpacing = 2.sp
                )
            }

            ClockStyle.STACKED -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = hourText,
                        color = mainTextColor,
                        fontSize = (82f * clockScale).sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = selectedFontFamily,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = minuteText,
                        color = mainTextColor,
                        fontSize = (82f * clockScale).sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = selectedFontFamily,
                        letterSpacing = 2.sp
                    )
                }
            }

            ClockStyle.MINIMAL -> {
                Text(
                    text = "$hourText · $minuteText",
                    color = mainTextColor,
                    fontSize = (96f * clockScale).sp,
                    fontWeight = FontWeight.ExtraLight,
                    fontFamily = selectedFontFamily,
                    letterSpacing = 4.sp
                )
            }

            ClockStyle.SPLIT -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = hourText,
                        color = mainTextColor,
                        fontSize = (96f * clockScale).sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = selectedFontFamily
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = "|",
                        color = secondaryTextColor,
                        fontSize = (64f * clockScale).sp,
                        fontWeight = FontWeight.ExtraLight
                    )

                    Text(
                        text = minuteText,
                        color = mainTextColor,
                        fontSize = (96f * clockScale).sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = selectedFontFamily
                    )
                }
            }

            ClockStyle.SECONDS -> {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = timeText,
                        color = mainTextColor,
                        fontSize = (96f * clockScale).sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = selectedFontFamily,
                        letterSpacing = 2.sp
                    )

                    Text(
                        modifier = Modifier.padding(
                            start = 12.dp,
                            bottom = 12.dp
                        ),
                        text = secondText,
                        color = secondaryTextColor,
                        fontSize = (34f * clockScale).sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = selectedFontFamily
                    )
                }
            }

            ClockStyle.ANALOG -> {
                AnalogClock(
                    hour = hourValue,
                    minute = minuteValue,
                    second = secondValue,
                    appColors = appColors,
                    clockSize = analogClockSize
                )
            }

            ClockStyle.FOCUS -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = hourText,
                        color = mainTextColor,
                        fontSize = (116f * clockScale).sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = selectedFontFamily
                    )

                    Text(
                        modifier = Modifier.padding(
                            start = 14.dp,
                            top = 28.dp
                        ),
                        text = minuteText,
                        color = secondaryTextColor,
                        fontSize = (48f * clockScale).sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = selectedFontFamily
                    )
                }
            }

            ClockStyle.WIDE -> {
                Row(
                    modifier = Modifier.width(
                        (430f * clockScale).dp
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = hourText,
                            color = mainTextColor,
                            fontSize = (104f * clockScale).sp,
                            fontWeight = FontWeight.ExtraLight,
                            fontFamily = selectedFontFamily,
                            letterSpacing = 3.sp
                        )

                        Text(
                            text = "HOUR",
                            color = secondaryTextColor,
                            fontSize = (11f * clockScale).sp,
                            fontWeight = FontWeight.Light,
                            letterSpacing = 3.sp
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = minuteText,
                            color = mainTextColor,
                            fontSize = (104f * clockScale).sp,
                            fontWeight = FontWeight.ExtraLight,
                            fontFamily = selectedFontFamily,
                            letterSpacing = 3.sp
                        )

                        Text(
                            text = "MINUTE",
                            color = secondaryTextColor,
                            fontSize = (11f * clockScale).sp,
                            fontWeight = FontWeight.Light,
                            letterSpacing = 3.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = dateText,
            color = secondaryTextColor,
            fontSize = dateFontSize,
            fontWeight = FontWeight.Light
        )


    }
}
