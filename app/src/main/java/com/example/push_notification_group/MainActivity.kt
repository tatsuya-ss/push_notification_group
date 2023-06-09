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
            val childNotification = createPushNotification(MESSAGE_NOTIFICATION_GROUP_A)
            sendNotification(childNotification, System.currentTimeMillis().toInt())
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

    private fun createPushNotification(groupId: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("ハロー $pushNotificationCount")
            .setContentText(System.currentTimeMillis().toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(groupId)
            .build()
    }

    private fun sendNotification(childNotification: Notification, notificationId: Int) {
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, childNotification)
        }
    }

    companion object {
        private const val CHANNEL_ID = "0"
        private const val MESSAGE_NOTIFICATION_GROUP_A = "message_notification_group_a"
    }
}


