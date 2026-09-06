package com.samuel.nightclock.ui.clock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.samuel.nightclock.NightClockUiColors
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun AnalogClock(
    hour: Int,
    minute: Int,
    second: Int,
    appColors: NightClockUiColors,
    modifier: Modifier = Modifier,
    clockSize: Dp = 220.dp
){
    Canvas(
        modifier = modifier.size(clockSize)
    ) {
        val center = Offset(
            x = size.width / 2f,
            y = size.height / 2f
        )

        val radius =
            min(size.width, size.height) / 2f

        drawCircle(
            color = appColors.track,
            radius = radius - 2.dp.toPx(),
            center = center,
            style = Stroke(
                width = 1.dp.toPx()
            )
        )

        repeat(12) { index ->
            val angle =
                Math.toRadians(
                    index * 30.0 - 90.0
                )

            val isMainMarker =
                index % 3 == 0

            val outerRadius =
                radius - 10.dp.toPx()

            val innerRadius =
                radius - if (isMainMarker) {
                    22.dp.toPx()
                } else {
                    17.dp.toPx()
                }

            val start = Offset(
                x = center.x +
                        cos(angle).toFloat() * innerRadius,
                y = center.y +
                        sin(angle).toFloat() * innerRadius
            )

            val end = Offset(
                x = center.x +
                        cos(angle).toFloat() * outerRadius,
                y = center.y +
                        sin(angle).toFloat() * outerRadius
            )

            drawLine(
                color = if (isMainMarker) {
                    appColors.secondary
                } else {
                    appColors.track
                },
                start = start,
                end = end,
                strokeWidth = if (isMainMarker) {
                    2.dp.toPx()
                } else {
                    1.dp.toPx()
                },
                cap = StrokeCap.Round
            )
        }

        val hourAngle =
            ((hour % 12) + minute / 60f) *
                    30f - 90f

        val minuteAngle =
            (minute + second / 60f) *
                    6f - 90f

        val secondAngle =
            second * 6f - 90f

        drawClockHand(
            center = center,
            angleDegrees = hourAngle,
            length = radius * 0.48f,
            color = appColors.main,
            strokeWidth = 6.dp.toPx()
        )

        drawClockHand(
            center = center,
            angleDegrees = minuteAngle,
            length = radius * 0.70f,
            color = appColors.main,
            strokeWidth = 4.dp.toPx()
        )

        drawClockHand(
            center = center,
            angleDegrees = secondAngle,
            length = radius * 0.77f,
            color = appColors.secondary,
            strokeWidth = 1.5.dp.toPx()
        )

        drawCircle(
            color = appColors.main,
            radius = 5.dp.toPx(),
            center = center
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawClockHand(
    center: Offset,
    angleDegrees: Float,
    length: Float,
    color: androidx.compose.ui.graphics.Color,
    strokeWidth: Float
) {
    val angle =
        Math.toRadians(angleDegrees.toDouble())

    val end = Offset(
        x = center.x +
                cos(angle).toFloat() * length,
        y = center.y +
                sin(angle).toFloat() * length
    )

    drawLine(
        color = color,
        start = center,
        end = end,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}