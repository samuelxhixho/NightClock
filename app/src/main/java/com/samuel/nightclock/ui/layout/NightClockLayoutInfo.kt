package com.samuel.nightclock.ui.layout

import androidx.compose.ui.unit.Dp

enum class NightClockScreenSize {
    COMPACT,
    MEDIUM,
    EXPANDED
}

data class NightClockLayoutInfo(
    val screenSize: NightClockScreenSize,
    val width: Dp,
    val height: Dp
)

fun createNightClockLayoutInfo(
    width: Dp,
    height: Dp
): NightClockLayoutInfo {
    val shortestSide = minOf(
        width.value,
        height.value
    )

    val screenSize = when {
        shortestSide < 480f ->
            NightClockScreenSize.COMPACT

        shortestSide < 700f ->
            NightClockScreenSize.MEDIUM

        else ->
            NightClockScreenSize.EXPANDED
    }

    return NightClockLayoutInfo(
        screenSize = screenSize,
        width = width,
        height = height
    )
}