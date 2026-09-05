package com.samuel.nightclock.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

fun vibrateTimerFinished(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager

        manager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (!vibrator.hasVibrator()) return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(0, 600, 200, 600, 200, 900),
                intArrayOf(0, 255, 0, 255, 0, 255),
                -1
            )
        )
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(
            longArrayOf(0, 600, 200, 600, 200, 900),
            -1
        )
    }
}

fun playTimerFinishedSound() {
    val toneGenerator = ToneGenerator(
        AudioManager.STREAM_ALARM,
        80
    )

    toneGenerator.startTone(
        ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,
        1200
    )

    Handler(Looper.getMainLooper()).postDelayed({
        toneGenerator.release()
    }, 1400)
}