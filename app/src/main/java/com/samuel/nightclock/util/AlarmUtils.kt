package com.samuel.nightclock.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.samuel.nightclock.R
import com.samuel.nightclock.model.AlarmSound

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

private var activeAlarmPlayer: MediaPlayer? = null

fun playTimerFinishedSound(
    context: Context,
    alarmSound: AlarmSound
) {
    stopTimerFinishedSound()

    val soundResource = when (alarmSound) {
        AlarmSound.SOFT -> R.raw.alarm_soft
        AlarmSound.DIGITAL -> R.raw.alarm_digital
        AlarmSound.BELL -> R.raw.alarm_bell
        AlarmSound.PULSE -> R.raw.alarm_pulse
    }

    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ALARM)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    val assetFileDescriptor =
        context.resources.openRawResourceFd(soundResource)

    val mediaPlayer = MediaPlayer()

    mediaPlayer.setAudioAttributes(audioAttributes)

    mediaPlayer.setDataSource(
        assetFileDescriptor.fileDescriptor,
        assetFileDescriptor.startOffset,
        assetFileDescriptor.length
    )

    assetFileDescriptor.close()

    mediaPlayer.setOnCompletionListener { completedPlayer ->
        if (activeAlarmPlayer === completedPlayer) {
            activeAlarmPlayer = null
        }

        completedPlayer.release()
    }

    mediaPlayer.prepare()

    activeAlarmPlayer = mediaPlayer

    mediaPlayer.start()
}

fun stopTimerFinishedSound() {
    activeAlarmPlayer?.let { mediaPlayer ->
        runCatching {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
        }

        mediaPlayer.release()
    }

    activeAlarmPlayer = null
}