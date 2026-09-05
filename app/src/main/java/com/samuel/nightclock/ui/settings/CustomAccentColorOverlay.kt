package com.samuel.nightclock.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.heightIn
import kotlin.math.roundToInt

@Composable
fun CustomAccentColorOverlay(
    initialColor: Long,
    onSave: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var red by remember(initialColor) {
        mutableFloatStateOf(
            ((initialColor shr 16) and 0xFF).toFloat()
        )
    }

    var green by remember(initialColor) {
        mutableFloatStateOf(
            ((initialColor shr 8) and 0xFF).toFloat()
        )
    }

    var blue by remember(initialColor) {
        mutableFloatStateOf(
            (initialColor and 0xFF).toFloat()
        )
    }

    val previewColor = Color(
        red = red / 255f,
        green = green / 255f,
        blue = blue / 255f,
        alpha = 1f
    )

    val redValue = red.roundToInt().coerceIn(0, 255)
    val greenValue = green.roundToInt().coerceIn(0, 255)
    val blueValue = blue.roundToInt().coerceIn(0, 255)

    val hexColor = String.format(
        "#%02X%02X%02X",
        redValue,
        greenValue,
        blueValue
    )

    Column(
        modifier = Modifier
            .width(390.dp)
            .heightIn(max = 300.dp)
            .background(
                color = Color(0xFF101010),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFF242424),
                shape = RoundedCornerShape(24.dp)
            )
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Custom accent",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light
            )

            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Close",
                    color = Color(0xFF888888)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    color = previewColor,
                    shape = RoundedCornerShape(100.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(100.dp)
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = hexColor,
            color = Color(0xFF888888),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(14.dp))


        ColorChannelSlider(
            label = "Red",
            value = red,
            onValueChange = {
                red = it
            }
        )

        ColorChannelSlider(
            label = "Green",
            value = green,
            onValueChange = {
                green = it
            }
        )

        ColorChannelSlider(
            label = "Blue",
            value = blue,
            onValueChange = {
                blue = it
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                val colorArgb =
                    (0xFFL shl 24) or
                            (redValue.toLong() shl 16) or
                            (greenValue.toLong() shl 8) or
                            blueValue.toLong()

                onSave(colorArgb)
            },
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Save color",
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
private fun ColorChannelSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = Color(0xFFAAAAAA),
                fontSize = 13.sp
            )

            Text(
                text = value.roundToInt().toString(),
                color = Color.White,
                fontSize = 13.sp
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..255f
        )
    }
}