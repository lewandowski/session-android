package org.thoughtcrime.securesms.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import org.thoughtcrime.securesms.dependencies.CallComponent
import org.thoughtcrime.securesms.webrtc.AudioManagerCommand
import org.thoughtcrime.securesms.webrtc.CallManager
import org.thoughtcrime.securesms.webrtc.audio.SignalAudioManager
import java.sql.CallableStatement
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates
import kotlin.properties.Delegates.observable

@AndroidEntryPoint
class WebRtcCallService: Service(), SignalAudioManager.EventListener {

    @Inject lateinit var callManager: CallManager
    val signalAudioManager: SignalAudioManager by lazy {
        SignalAudioManager(this, this, CallComponent.get(this).callManagerCompat())
    }

    private enum class CallState {
        STATE_IDLE, STATE_DIALING, STATE_ANSWERING, STATE_REMOTE_RINGING, STATE_LOCAL_RINGING, STATE_CONNECTED
    }

    companion object {
        private const val ACTION_UPDATE = "UPDATE"
        private const val ACTION_STOP = "STOP"
        private const val ACTION_DENY_CALL = "DENY_CALL"
        private const val ACTION_LOCAL_HANGUP = "LOCAL_HANGUP"
        private const val ACTION_CHANGE_POWER_BUTTON = "CHANGE_POWER_BUTTON"
        private const val ACTION_SEND_AUDIO_COMMAND = "SEND_AUDIO_COMMAND"

        private const val EXTRA_UPDATE_TYPE = "UPDATE_TYPE"
        private const val EXTRA_RECIPIENT_ID = "RECIPIENT_ID"
        private const val EXTRA_ENABLED = "ENABLED"
        private const val EXTRA_AUDIO_COMMAND = "AUDIO_COMMAND"

        private const val INVALID_NOTIFICATION_ID = -1

        private var lastNotificationId: Int = INVALID_NOTIFICATION_ID
        private var lastNotification: Notification? = null


        fun update(context: Context, type: Int, callId: UUID) {
            val intent = Intent(context, WebRtcCallService::class.java)
                .setAction(ACTION_UPDATE)
                .putExtra(EXTRA_RECIPIENT_ID, callId)
                .putExtra(EXTRA_UPDATE_TYPE, type)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            val intent = Intent(context, WebRtcCallService::class.java)
                .setAction(ACTION_STOP)
            ContextCompat.startForegroundService(context, intent)
        }

        fun denyCallIntent(context: Context) = Intent(context, WebRtcCallService::class.java).setAction(ACTION_DENY_CALL)

        fun hangupIntent(context: Context) = Intent(context, WebRtcCallService::class.java).setAction(ACTION_LOCAL_HANGUP)

        fun sendAudioManagerCommand(context: Context, command: AudioManagerCommand) {
            val intent = Intent(context, WebRtcCallService::class.java)
                .setAction(ACTION_SEND_AUDIO_COMMAND)
                .putExtra(EXTRA_AUDIO_COMMAND, command)
            ContextCompat.startForegroundService(context, intent)
        }

        fun changePowerButtonReceiver(context: Context, register: Boolean) {
            val intent = Intent(context, WebRtcCallService::class.java)
                .setAction(ACTION_CHANGE_POWER_BUTTON)
                .putExtra(EXTRA_ENABLED, register)
            ContextCompat.startForegroundService(context, intent)
        }
    }

    private var state: CallState by observable(CallState.STATE_IDLE) { _, previousValue, newValue ->

    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        // create audio manager
        // reset call notification
        // register uncaught exception handler
        // register network receiver
        // telephony listen to call state
    }

    override fun onDestroy() {
        super.onDestroy()
        // unregister exception handler
        // shutdown audiomanager
        // unregister network receiver
        // unregister power button
    }

    override fun onAudioDeviceChanged(activeDevice: SignalAudioManager.AudioDevice, devices: Set<SignalAudioManager.AudioDevice>) {
        TODO("Not yet implemented")
    }
}