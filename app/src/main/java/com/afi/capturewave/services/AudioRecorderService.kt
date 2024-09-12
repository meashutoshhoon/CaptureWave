package com.afi.capturewave.services

import android.content.pm.ServiceInfo
import android.media.MediaRecorder
import android.os.Build
import android.widget.Toast
import com.afi.capturewave.App
import com.afi.capturewave.R
import com.afi.capturewave.enums.AudioChannels
import com.afi.capturewave.enums.AudioDeviceSource
import com.afi.capturewave.obj.AudioFormat
import com.afi.capturewave.util.PlayerHelper
import com.afi.capturewave.util.Preferences

class AudioRecorderService : RecorderService() {
    override val notificationTitle: String
        get() = getString(R.string.recording_audio)

    override val fgServiceType: Int?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
        } else {
            null
        }

    override fun start() {
        val audioFormat = AudioFormat.getCurrent()

        recorder = PlayerHelper.newRecorder(this).apply {
            val audioSource = Preferences.prefs.getInt(
                Preferences.audioDeviceSourceKey,
                AudioDeviceSource.DEFAULT.value
            )
            setAudioSource(audioSource)

            val sampleRatePref = Preferences.prefs.getInt(Preferences.audioSampleRateKey, -1).takeIf { it > 0 }
            val audioBitrate = Preferences.prefs.getInt(Preferences.audioBitrateKey, -1).takeIf { it > 0 }
            if (sampleRatePref != null && (audioFormat.codec != MediaRecorder.AudioEncoder.OPUS || sampleRatePref in opusSampleRates)) {
                setAudioSamplingRate(sampleRatePref)
            }
            if (audioBitrate != null) {
                setAudioEncodingBitRate(audioBitrate)
            } else if (sampleRatePref != null) {
                setAudioEncodingBitRate(sampleRatePref * 32 * 2)
            }

            Preferences.prefs.getInt(Preferences.audioChannelsKey, AudioChannels.MONO.value).let {
                setAudioChannels(it)
            }

            setOutputFormat(audioFormat.format)
            setAudioEncoder(audioFormat.codec)

            outputFile = (application as App).fileRepository.getOutputFile(
                audioFormat.extension
            )
            if (outputFile == null) {
                Toast.makeText(
                    this@AudioRecorderService,
                    R.string.cant_access_selected_folder,
                    Toast.LENGTH_LONG
                ).show()
                onDestroy()
                return
            }

            fileDescriptor = contentResolver.openFileDescriptor(outputFile!!.uri, "w")
            setOutputFile(fileDescriptor?.fileDescriptor)

            runCatching {
                prepare()
            }

            start()
        }

        super.start()
    }

    override fun getCurrentAmplitude() = recorder?.maxAmplitude

    companion object {
        private val opusSampleRates =  listOf(8000, 12000, 16000, 24000, 48000)
    }
}