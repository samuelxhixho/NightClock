package com.samuel.nightclock.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
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

    vibrator.vibrate(
        VibrationEffect.createWaveform(
            longArrayOf(0, 250, 150, 250),
            -1
        )
    )
}

private var activeAlarmPlayer: MediaPlayer? = null

private var activeVolumeJob: Job? = null

fun playTimerFinishedSound(
    context: Context,
    alarmSound: AlarmSound,
    volumePercent: Int,
    gradualAlarmEnabled: Boolean,
    loop: Boolean
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

    val volume = volumePercent
        .coerceIn(0, 100) / 100f

    mediaPlayer.setAudioAttributes(audioAttributes)

    mediaPlayer.isLooping = loop

    if (gradualAlarmEnabled) {
        mediaPlayer.setVolume(
            0f,
            0f
        )
    } else {
        mediaPlayer.setVolume(
            volume,
            volume
        )
    }

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

    if (gradualAlarmEnabled && volume > 0f) {
        activeVolumeJob = CoroutineScope(Dispatchers.Main).launch {
            val steps = 40
            val stepDelayMillis = 8_000L / steps

            repeat(steps) { index ->
                if (activeAlarmPlayer !== mediaPlayer) {
                    return@launch
                }

                val progress =
                    (index + 1).toFloat() / steps

                val currentVolume =
                    volume * progress

                mediaPlayer.setVolume(
                    currentVolume,
                    currentVolume
                )

                delay(stepDelayMillis.milliseconds)
            }
        }
    }
}

fun stopTimerFinishedSound() {

    activeVolumeJob?.cancel()
    activeVolumeJob = null

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