package com.samuel.nightclock.ui.timer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuel.nightclock.NightClockUiColors
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun CustomTimerOverlay(
    modifier: Modifier = Modifier,
    initialMinutes: Int,
    appColors: NightClockUiColors,
    onStart: (Int) -> Unit,
    onClose: () -> Unit
) {
    var hours by remember(initialMinutes) {
        mutableIntStateOf((initialMinutes / 60).coerceIn(0, 23))
    }

    var minutes by remember(initialMinutes) {
        mutableIntStateOf((initialMinutes % 60).coerceIn(0, 59))
    }

    val totalMinutes = hours * 60 + minutes
    val canStart = totalMinutes > 0

    Column(
        modifier = modifier
            .width(430.dp)
            .background(
                color = Color(0xFF080808),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.width(394.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Custom timer",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraLight
            )

            TextButton(
                onClick = onClose,
                contentPadding = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 0.dp
                )
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

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeUnitControl(
                label = "hours",
                value = hours,
                range = 0..23,
                onValueSelected = { selectedHour ->
                    hours = selectedHour
                },
                appColors = appColors
            )

            Text(
                modifier = Modifier.padding(horizontal = 18.dp),
                text = ":",
                color = appColors.secondary,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraLight
            )

            TimeUnitControl(
                label = "minutes",
                value = minutes,
                range = 0..59,
                onValueSelected = { selectedMinute ->
                    minutes = selectedMinute
                },
                appColors = appColors
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            enabled = canStart,
            onClick = {
                onStart(totalMinutes)
            },
            shape = RoundedCornerShape(100.dp),
            border = BorderStroke(
                width = 1.dp,
                color = appColors.buttonBorder
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.buttonBackground,
                contentColor = appColors.main,
                disabledContainerColor = Color(0xFF090909),
                disabledContentColor = Color(0xFF444444)
            ),
            contentPadding = PaddingValues(
                horizontal = 28.dp,
                vertical = 8.dp
            )
        ) {
            Text(
                text = "Start",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
private fun TimeUnitControl(
    label: String,
    value: Int,
    range: IntRange,
    onValueSelected: (Int) -> Unit,
    appColors: NightClockUiColors
) {
    val values = range.toList()
    val selectedIndex = (value - range.first)
        .coerceIn(0, values.lastIndex)

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedIndex
    )

    val scope = rememberCoroutineScope()
    val flingBehavior = rememberSnapFlingBehavior(listState)

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling) {
                    val layoutInfo = listState.layoutInfo
                    val viewportCenter =
                        (layoutInfo.viewportStartOffset +
                                layoutInfo.viewportEndOffset) / 2

                    val centeredItem =
                        layoutInfo.visibleItemsInfo.minByOrNull { item ->
                            val itemCenter =
                                item.offset + item.size / 2

                            abs(itemCenter - viewportCenter)
                        }

                    centeredItem?.let { item ->
                        val selectedValue = values[item.index]

                        if (selectedValue != value) {
                            onValueSelected(selectedValue)
                        }
                    }
                }
            }
    }

    Column(
        modifier = Modifier.width(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.height(156.dp),
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = 52.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                count = values.size,
                key = { index -> values[index] }
            ) { index ->
                val option = values[index]
                val isSelected = option == value

                Box(
                    modifier = Modifier
                        .height(52.dp)
                        .width(82.dp)
                        .clickable {
                            onValueSelected(option)

                            scope.launch {
                                listState.animateScrollToItem(index)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "%02d".format(option),
                        color = if (isSelected) {
                            appColors.main
                        } else {
                            appColors.secondary
                        },
                        fontSize = if (isSelected) {
                            36.sp
                        } else {
                            22.sp
                        },
                        fontWeight = if (isSelected) {
                            FontWeight.Light
                        } else {
                            FontWeight.ExtraLight
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            color = appColors.secondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )
    }
}