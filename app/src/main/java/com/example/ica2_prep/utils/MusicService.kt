package com.example.ica2_prep.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.ica2_prep.R

class MusicService : Service() {
    /*
    In manifest:
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK" />

    In <application> in manifest:
    <service android:name=".utils.MusicService" android:foregroundServiceType="mediaPlayback"/>

    In raw:
    replace song.mp3 with suitable song

    To start:
    context.startService(Intent(context, MusicService::class.java).apply { action = "START" })

    To stop:
    context.stopService(Intent(context, MusicService::class.java).apply { action = "STOP" })
     */

    private lateinit var player: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this, R.raw.song)
        player.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "START") {
            startForeground(1, createNotification())
            player.start()
        } else if (intent?.action == "STOP") {
            player.stop()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    private fun createNotification(): Notification {
        val channelId = "music_channel"
        val channel = NotificationChannel(
            channelId, "Music Playback",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)

        val stopIntent = Intent(this, MusicService::class.java).apply { action = "STOP" }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Playing Music")
            .setSmallIcon(R.drawable.notification_icon)
            .addAction(R.drawable.notification_icon, "Stop", stopPendingIntent)
            .setOngoing(true)
            .build()
    }
}