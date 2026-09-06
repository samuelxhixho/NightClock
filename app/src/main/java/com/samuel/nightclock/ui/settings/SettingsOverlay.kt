package com.samuel.nightclock.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Slider
import com.samuel.nightclock.model.AlarmSound
import com.samuel.nightclock.model.ClockStyle
import com.samuel.nightclock.model.ClockFont
import com.samuel.nightclock.model.AccentColorPreset
import com.samuel.nightclock.ui.layout.NightClockLayoutInfo
import com.samuel.nightclock.ui.layout.NightClockScreenSize

@Composable
fun SettingsOverlay(
    modifier: Modifier = Modifier,
    layoutInfo: NightClockLayoutInfo,
    soundEnabled: Boolean,
    alarmSound: AlarmSound,
    alarmVolume: Int,
    gradualAlarmEnabled: Boolean,
    vibrationEnabled: Boolean,
    batteryWarningEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    onAlarmSoundChange: (AlarmSound) -> Unit,
    onPreviewAlarmSound: () -> Unit,
    onAlarmVolumeChange: (Int) -> Unit,
    onGradualAlarmChange: (Boolean) -> Unit,
    onVibrationChange: (Boolean) -> Unit,
    onBatteryWarningChange: (Boolean) -> Unit,
    dimModeEnabled: Boolean,
    onDimModeChange: (Boolean) -> Unit,
    autoDimEnabled: Boolean,
    autoDimStartMinutes: Int,
    autoDimEndMinutes: Int,
    onAutoDimChange: (Boolean) -> Unit,
    onEditAutoDimStart: () -> Unit,
    onEditAutoDimEnd: () -> Unit,
    clockStyle: ClockStyle,
    onClockStyleChange: (ClockStyle) -> Unit,
    accentColorPreset: AccentColorPreset,
    customAccentColor: Long,
    onAccentColorChange: (AccentColorPreset) -> Unit,
    onEditCustomAccentColor: () -> Unit,
    clockFont: ClockFont,
    onClockFontChange: (ClockFont) -> Unit,
    timerPreset1: Int,
    timerPreset2: Int,
    timerPreset3: Int,
    onEditPreset1: () -> Unit,
    onEditPreset2: () -> Unit,
    onEditPreset3: () -> Unit,
    onClose: () -> Unit
) {
    val settingsWidth = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT ->
            (layoutInfo.width - 32.dp).coerceAtMost(430.dp)

        NightClockScreenSize.MEDIUM ->
            (layoutInfo.width - 64.dp).coerceAtMost(560.dp)

        NightClockScreenSize.EXPANDED ->
            (layoutInfo.width - 96.dp).coerceAtMost(760.dp)
    }

    val settingsMaxHeight = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT ->
            (layoutInfo.height - 32.dp).coerceAtMost(330.dp)

        NightClockScreenSize.MEDIUM ->
            (layoutInfo.height - 64.dp).coerceAtMost(460.dp)

        NightClockScreenSize.EXPANDED ->
            (layoutInfo.height - 96.dp).coerceAtMost(620.dp)
    }

    val settingsPadding = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 18.dp
        NightClockScreenSize.MEDIUM -> 22.dp
        NightClockScreenSize.EXPANDED -> 26.dp
    }

    val isExpanded =
        layoutInfo.screenSize == NightClockScreenSize.EXPANDED

    val appearanceContent: @Composable () -> Unit = {
        ClockStylePicker(
            selectedStyle = clockStyle,
            onStyleSelected = onClockStyleChange,
            itemsPerRow = if (isExpanded) 3 else 4
        )

        Spacer(modifier = Modifier.height(12.dp))

        ClockFontPicker(
            selectedFont = clockFont,
            onFontSelected = onClockFontChange,
            itemsPerRow = if (isExpanded) 2 else 4
        )

        Spacer(modifier = Modifier.height(12.dp))

        AccentColorPicker(
            selectedAccentColor = accentColorPreset,
            customAccentColor = customAccentColor,
            onAccentColorSelected = onAccentColorChange,
            onEditCustomColor = onEditCustomAccentColor
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Quick timer presets",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PresetButton(
                text = formatPresetLabel(timerPreset1),
                onClick = onEditPreset1
            )

            Spacer(modifier = Modifier.width(8.dp))

            PresetButton(
                text = formatPresetLabel(timerPreset2),
                onClick = onEditPreset2
            )

            Spacer(modifier = Modifier.width(8.dp))

            PresetButton(
                text = formatPresetLabel(timerPreset3),
                onClick = onEditPreset3
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        SettingsRow(
            title = "Dim mode",
            subtitle = "Softer colors for night use",
            checked = dimModeEnabled,
            onCheckedChange = onDimModeChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsRow(
            title = "Auto dim",
            subtitle = "Dim automatically on a schedule",
            checked = autoDimEnabled,
            onCheckedChange = onAutoDimChange
        )

        if (autoDimEnabled) {
            Spacer(modifier = Modifier.height(8.dp))

            AutoDimScheduleRow(
                startMinutes = autoDimStartMinutes,
                endMinutes = autoDimEndMinutes,
                onEditStart = onEditAutoDimStart,
                onEditEnd = onEditAutoDimEnd
            )
        }
    }

    val alarmContent: @Composable () -> Unit = {
        SettingsRow(
            title = "Sound",
            subtitle = "Play alarm when timer ends",
            checked = soundEnabled,
            onCheckedChange = onSoundChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        AlarmSoundPicker(
            selectedSound = alarmSound,
            onSoundSelected = onAlarmSoundChange,
            onPreview = onPreviewAlarmSound
        )

        Spacer(modifier = Modifier.height(12.dp))

        AlarmVolumeControl(
            volume = alarmVolume,
            onVolumeChange = onAlarmVolumeChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsRow(
            title = "Gradual alarm",
            subtitle = "Fade in alarm volume gradually",
            checked = gradualAlarmEnabled,
            onCheckedChange = onGradualAlarmChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsRow(
            title = "Vibration",
            subtitle = "Vibrate when timer ends",
            checked = vibrationEnabled,
            onCheckedChange = onVibrationChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsRow(
            title = "Battery warning",
            subtitle = "Warn when battery saver is on",
            checked = batteryWarningEnabled,
            onCheckedChange = onBatteryWarningChange
        )
    }

    Column(
        modifier = modifier
            .width(settingsWidth)
            .heightIn(max = settingsMaxHeight)
            .background(
                color = Color(0xFF080808),
                shape = RoundedCornerShape(26.dp)
            )
            .verticalScroll(rememberScrollState())
            .padding(settingsPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
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

        if (isExpanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(28.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    appearanceContent()
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    alarmContent()
                }
            }
        } else {
            appearanceContent()

            Spacer(modifier = Modifier.height(12.dp))

            alarmContent()
        }
    }
}

@Composable
private fun ClockStylePicker(
    selectedStyle: ClockStyle,
    onStyleSelected: (ClockStyle) -> Unit,
    itemsPerRow: Int = 4
) {
    val styles = listOf(
        ClockStyle.CLASSIC,
        ClockStyle.STACKED,
        ClockStyle.MINIMAL,
        ClockStyle.SPLIT,
        ClockStyle.SECONDS,
        ClockStyle.ANALOG,
        ClockStyle.FOCUS,
        ClockStyle.WIDE
    )

    Column {
        Text(
            text = "Clock style",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            styles.chunked(itemsPerRow).forEach { rowStyles ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowStyles.forEach { style ->
                        Button(
                            modifier = Modifier.width(90.dp),
                            onClick = {
                                onStyleSelected(style)
                            },
                            shape = RoundedCornerShape(100.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (selectedStyle == style) {
                                    Color(0xFFBDBDBD)
                                } else {
                                    Color(0xFF242424)
                                }
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedStyle == style) {
                                    Color(0xFF1A1A1A)
                                } else {
                                    Color(0xFF090909)
                                },
                                contentColor = if (selectedStyle == style) {
                                    Color.White
                                } else {
                                    Color(0xFF8A8A8A)
                                }
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 6.dp,
                                vertical = 6.dp
                            )
                        ) {
                            Text(
                                text = style.displayName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Light,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccentColorPicker(
    selectedAccentColor: AccentColorPreset,
    customAccentColor: Long,
    onAccentColorSelected: (AccentColorPreset) -> Unit,
    onEditCustomColor: () -> Unit
) {
    val colors = listOf(
        AccentColorPreset.WHITE to Color.White,
        AccentColorPreset.RED to Color(0xFFFF5A5F),
        AccentColorPreset.BLUE to Color(0xFF64B5F6),
        AccentColorPreset.GREEN to Color(0xFF66BB6A),
        AccentColorPreset.PURPLE to Color(0xFFB388FF),
        AccentColorPreset.AMBER to Color(0xFFFFC857),
        AccentColorPreset.CYAN to Color(0xFF4DD0E1),
        AccentColorPreset.PINK to Color(0xFFFF80AB),
        AccentColorPreset.ORANGE to Color(0xFFFF9F43),
        AccentColorPreset.CUSTOM to Color(customAccentColor)
    )

    Column {
        Text(
            text = "Accent color",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            colors.chunked(5).forEach { rowColors ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowColors.forEach { colorOption ->
                        val preset = colorOption.first
                        val color = colorOption.second

                        if (preset == AccentColorPreset.CUSTOM) {
                            Row(
                                modifier = Modifier
                                    .height(30.dp)
                                    .background(
                                        color = Color(0xFF101010),
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .border(
                                        width = if (
                                            selectedAccentColor == preset
                                        ) {
                                            2.dp
                                        } else {
                                            1.dp
                                        },
                                        color = if (
                                            selectedAccentColor == preset
                                        ) {
                                            Color.White
                                        } else {
                                            Color(0xFF333333)
                                        },
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .clickable {
                                        onEditCustomColor()
                                    }
                                    .padding(horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(
                                            color = color,
                                            shape = RoundedCornerShape(100.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color.White.copy(alpha = 0.35f),
                                            shape = RoundedCornerShape(100.dp)
                                        )
                                )

                                Spacer(
                                    modifier = Modifier.width(6.dp)
                                )

                                Text(
                                    text = preset.displayName,
                                    color = Color(0xFFBDBDBD),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(
                                        color = color,
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .border(
                                        width = if (
                                            selectedAccentColor == preset
                                        ) {
                                            2.dp
                                        } else {
                                            1.dp
                                        },
                                        color = if (
                                            selectedAccentColor == preset
                                        ) {
                                            Color.White
                                        } else {
                                            Color(0xFF333333)
                                        },
                                        shape = RoundedCornerShape(100.dp)
                                    )
                                    .clickable {
                                        onAccentColorSelected(preset)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )

            Text(
                modifier = Modifier.padding(top = 3.dp),
                text = subtitle,
                color = Color(0xFF666666),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.width(18.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun PresetButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF090909),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(
            horizontal = 14.dp,
            vertical = 6.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
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

@Composable
private fun AlarmSoundPicker(
    selectedSound: AlarmSound,
    onSoundSelected: (AlarmSound) -> Unit,
    onPreview: () -> Unit
) {
    Column {
        Text(
            text = "Alarm sound",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlarmSound.entries.forEachIndexed { index, sound ->
                Button(
                    onClick = {
                        onSoundSelected(sound)
                    },
                    shape = RoundedCornerShape(100.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (selectedSound == sound) {
                            Color(0xFFBDBDBD)
                        } else {
                            Color(0xFF242424)
                        }
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSound == sound) {
                            Color(0xFF1A1A1A)
                        } else {
                            Color(0xFF090909)
                        },
                        contentColor = if (selectedSound == sound) {
                            Color.White
                        } else {
                            Color(0xFF8A8A8A)
                        }
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    )
                ) {
                    Text(
                        text = sound.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Light
                    )
                }

                if (index != AlarmSound.entries.lastIndex) {
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        TextButton(
            onClick = onPreview
        ) {
            Text(
                text = "Preview",
                color = Color(0xFFBDBDBD),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
private fun AlarmVolumeControl(
    volume: Int,
    onVolumeChange: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Alarm volume",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Light
            )

            Text(
                text = "$volume%",
                color = Color(0xFF8A8A8A),
                fontSize = 13.sp,
                fontWeight = FontWeight.Light
            )
        }

        Slider(
            value = volume.toFloat(),
            onValueChange = { newValue ->
                onVolumeChange(
                    newValue.toInt().coerceIn(0, 100)
                )
            },
            valueRange = 0f..100f
        )
    }
}

@Composable
private fun AutoDimScheduleRow(
    startMinutes: Int,
    endMinutes: Int,
    onEditStart: () -> Unit,
    onEditEnd: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Start",
                color = Color(0xFF777777),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light
            )

            TextButton(
                onClick = onEditStart
            ) {
                Text(
                    text = formatTime(startMinutes),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }

        Text(
            text = "→",
            color = Color(0xFF666666),
            fontSize = 18.sp
        )

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "End",
                color = Color(0xFF777777),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light
            )

            TextButton(
                onClick = onEditEnd
            ) {
                Text(
                    text = formatTime(endMinutes),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

private fun formatTime(totalMinutes: Int): String {
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return "%02d:%02d".format(hours, minutes)
}

@Composable
private fun ClockFontPicker(
    selectedFont: ClockFont,
    onFontSelected: (ClockFont) -> Unit,
    itemsPerRow: Int = 4
) {
    Column {
        Text(
            text = "Clock font",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ClockFont.entries
                .toList()
                .chunked(itemsPerRow)
                .forEach { rowFonts ->

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rowFonts.forEach { font ->
                            Button(
                                modifier = Modifier.width(90.dp),
                                onClick = {
                                    onFontSelected(font)
                                },
                                shape = RoundedCornerShape(100.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (selectedFont == font) {
                                        Color(0xFFBDBDBD)
                                    } else {
                                        Color(0xFF242424)
                                    }
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (
                                        selectedFont == font
                                    ) {
                                        Color(0xFF1A1A1A)
                                    } else {
                                        Color(0xFF090909)
                                    },
                                    contentColor = if (
                                        selectedFont == font
                                    ) {
                                        Color.White
                                    } else {
                                        Color(0xFF8A8A8A)
                                    }
                                ),
                                contentPadding = PaddingValues(
                                    horizontal = 6.dp,
                                    vertical = 6.dp
                                )
                            ) {
                                Text(
                                    text = font.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Light,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
        }
    }
}