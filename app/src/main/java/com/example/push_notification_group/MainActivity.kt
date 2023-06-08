package com.example.push_notification_group

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.example.push_notification_group.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var pushNotificationCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        createNotificationChannel()

        binding.pushButton.setOnClickListener {
            pushNotificationCount ++
            val parentNotification = createParentNotification()
            val childNotification = createPushNotification()
            sendNotification(parentNotification, childNotification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "名前"
            val descriptionText = "説明"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPushNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("ハロー $pushNotificationCount")
            .setContentText(System.currentTimeMillis().toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(MESSAGE_NOTIFICATION_GROUP)
            .build()
    }

    private fun createParentNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("親通知")
            .setContentText(System.currentTimeMillis().toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(MESSAGE_NOTIFICATION_GROUP)
            .setGroupSummary(true)
            .build()
    }

    private fun sendNotification(parentNotification: Notification, childNotification: Notification) {
        with(NotificationManagerCompat.from(this)) {
            notify(pushNotificationCount, childNotification)
            notify(PARENT_NOTIFICATION_ID, parentNotification)
        }
    }

    companion object {
        private const val CHANNEL_ID = "0"
        private const val PARENT_NOTIFICATION_ID = 0
        private const val MESSAGE_NOTIFICATION_GROUP = "message_notification_group"
    }
}


