package com.samuel.nightclock.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuel.nightclock.ui.layout.NightClockLayoutInfo
import com.samuel.nightclock.ui.layout.NightClockScreenSize

@Composable
fun OnboardingScreen(
    layoutInfo: NightClockLayoutInfo,
    onFinish: () -> Unit
){
    val cardWidth = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT ->
            (layoutInfo.width - 32.dp).coerceAtMost(430.dp)

        NightClockScreenSize.MEDIUM ->
            (layoutInfo.width - 64.dp).coerceAtMost(500.dp)

        NightClockScreenSize.EXPANDED ->
            (layoutInfo.width - 96.dp).coerceAtMost(560.dp)
    }

    val horizontalPadding = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 28.dp
        NightClockScreenSize.MEDIUM -> 32.dp
        NightClockScreenSize.EXPANDED -> 36.dp
    }

    val verticalPadding = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 24.dp
        NightClockScreenSize.MEDIUM -> 28.dp
        NightClockScreenSize.EXPANDED -> 32.dp
    }

    val titleSize = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 24.sp
        NightClockScreenSize.MEDIUM -> 26.sp
        NightClockScreenSize.EXPANDED -> 28.sp
    }

    val descriptionSize = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 14.sp
        NightClockScreenSize.MEDIUM -> 15.sp
        NightClockScreenSize.EXPANDED -> 16.sp
    }

    val buttonWidth = when (layoutInfo.screenSize) {
        NightClockScreenSize.COMPACT -> 180.dp
        NightClockScreenSize.MEDIUM -> 200.dp
        NightClockScreenSize.EXPANDED -> 220.dp
    }

    var page by remember {
        mutableIntStateOf(0)
    }

    val pages = listOf(
        OnboardingPage(
            title = "Welcome to NightClock",
            description =
                "A minimalist OLED clock and timer designed for bedside and desk use."
        ),
        OnboardingPage(
            title = "OLED friendly",
            description =
                "NightClock subtly shifts the clock position to help reduce burn-in. Dim mode and Auto Dim can make night use more comfortable."
        ),
        OnboardingPage(
            title = "Reliable alarms",
            description =
                "For the most reliable timer sound and vibration, avoid aggressive Battery Saver modes while NightClock is running."
        )
    )

    val currentPage = pages[page]
    val isLastPage = page == pages.lastIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(cardWidth)
                .background(
                    color = Color(0xFF080808),
                    shape = RoundedCornerShape(28.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFF202020),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPadding
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NightClock",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = currentPage.title,
                color = Color.White,
                fontSize = titleSize,
                fontWeight = FontWeight.ExtraLight
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentPage.description,
                color = Color(0xFF8A8A8A),
                fontSize = descriptionSize,
                fontWeight = FontWeight.Light,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                pages.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .size(
                                if (index == page) {
                                    8.dp
                                } else {
                                    6.dp
                                }
                            )
                            .background(
                                color = if (index == page) {
                                    Color.White
                                } else {
                                    Color(0xFF3A3A3A)
                                },
                                shape = RoundedCornerShape(100.dp)
                            )
                            .clickable {
                                page = index
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (page > 0) {
                    TextButton(
                        onClick = {
                            page -= 1
                        }
                    ) {
                        Text(
                            text = "Back",
                            color = Color(0xFF777777),
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                Button(
                    modifier = Modifier.width(buttonWidth),
                    onClick = {
                        if (isLastPage) {
                            onFinish()
                        } else {
                            page += 1
                        }
                    },
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A1A1A),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (isLastPage) {
                            "Get started"
                        } else {
                            "Continue"
                        },
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}

private data class OnboardingPage(
    val title: String,
    val description: String
)