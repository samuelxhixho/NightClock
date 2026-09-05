package com.samuel.nightclock.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

@Composable
fun DismissibleOverlay(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(
        enabled = true,
        onBack = onDismiss
    )

    val backgroundInteractionSource = remember {
        MutableInteractionSource()
    }

    val contentInteractionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.72f))
            .clickable(
                interactionSource = backgroundInteractionSource,
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.clickable(
                interactionSource = contentInteractionSource,
                indication = null,
                onClick = {}
            )
        ) {
            content()
        }
    }
}