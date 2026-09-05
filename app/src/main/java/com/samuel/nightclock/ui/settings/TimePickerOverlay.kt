package com.samuel.nightclock.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
fun TimePickerOverlay(
    title: String,
    initialMinutes: Int,
    appColors: NightClockUiColors,
    onSave: (Int) -> Unit
) {
    var hours by remember(initialMinutes) {
        mutableIntStateOf(
            (initialMinutes / 60).coerceIn(0, 23)
        )
    }

    var minutes by remember(initialMinutes) {
        mutableIntStateOf(
            (initialMinutes % 60).coerceIn(0, 59)
        )
    }

    Column(
        modifier = Modifier
            .width(320.dp)
            .background(
                color = Color(0xFF080808),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraLight
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeWheel(
                value = hours,
                range = 0..23,
                onValueSelected = { selected ->
                    hours = selected
                },
                appColors = appColors
            )

            Text(
                modifier = Modifier.padding(horizontal = 14.dp),
                text = ":",
                color = appColors.secondary,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraLight
            )

            TimeWheel(
                value = minutes,
                range = 0..59,
                onValueSelected = { selected ->
                    minutes = selected
                },
                appColors = appColors
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSave(hours * 60 + minutes)
            },
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.buttonBackground,
                contentColor = appColors.main
            ),
            contentPadding = PaddingValues(
                horizontal = 26.dp,
                vertical = 8.dp
            )
        ) {
            Text(
                text = "Save",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
private fun TimeWheel(
    value: Int,
    range: IntRange,
    onValueSelected: (Int) -> Unit,
    appColors: NightClockUiColors
) {
    val values = range.toList()

    val selectedIndex =
        (value - range.first).coerceIn(0, values.lastIndex)

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
                        (
                                layoutInfo.viewportStartOffset +
                                        layoutInfo.viewportEndOffset
                                ) / 2

                    val centeredItem =
                        layoutInfo.visibleItemsInfo.minByOrNull { item ->
                            val itemCenter =
                                item.offset + item.size / 2

                            abs(itemCenter - viewportCenter)
                        }

                    centeredItem?.let { item ->
                        val selectedValue =
                            values[item.index]

                        if (selectedValue != value) {
                            onValueSelected(selectedValue)
                        }
                    }
                }
            }
    }

    LazyColumn(
        modifier = Modifier
            .width(90.dp)
            .height(156.dp),
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
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}