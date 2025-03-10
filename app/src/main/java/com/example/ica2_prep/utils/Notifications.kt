package com.example.ica2_prep.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.ica2_prep.R

/*
In manifest:
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>

In strings.xml:
<string name="group_key">group_key</string>
<string name="channel_id">channel_id</string>

In drawable:
replace notification_icon.png with suitable image
 */

fun createNotificationChannel(context: Context) {
    // To be called in MainActivity.onCreate()

    val channel = NotificationChannel(
        context.getString(R.string.channel_id),
        "General Notifications",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Channel for general notifications"
    }

    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)
}

@Composable
fun RequestNotificationPermission(onPermissionGranted: () -> Unit) {
    // To be called at start of main screen
    // Eg: RequestNotificationPermission { isNotificationsEnabled = true }

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT)
                .show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

fun showNotification(context: Context, id: Int, title: String, message: String) {
    // Call multiple times with incrementing id to show multiple notifications
    // Eg:
    // messages.forEachIndexed { index, message ->
    //     showNotification(context, index, "Message $index", message)
    // }

    val notificationManager =
        ContextCompat.getSystemService(context, NotificationManager::class.java)
    val groupKey = context.getString(R.string.group_key)

    // Individual Notification
    val builder = NotificationCompat.Builder(context, context.getString(R.string.channel_id))
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setGroup(groupKey)
        .setAutoCancel(true)

    notificationManager?.notify(id, builder.build())

    // Summary Notification (Shows only when multiple exist)
    val summaryBuilder =
        NotificationCompat.Builder(context, context.getString(R.string.channel_id))
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("You have new messages")
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("$title: $message")
                    .setSummaryText("Multiple notifications")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(groupKey)
            .setGroupSummary(true)

    notificationManager?.notify(1000, summaryBuilder.build())
}