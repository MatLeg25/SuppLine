package io.suppline.presentation.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import io.suppline.presentation.MainActivity

class NotificationSnoozeBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == MainActivity.ACTION_SNOOZE) {
            val notificationId = intent.getIntExtra(MainActivity.EXTRA_NOTIFICATION_ID, 0)
            // Handle the snooze action
            Toast.makeText(context, "Snooze action triggered for notification ID: $notificationId", Toast.LENGTH_SHORT).show()

            // Optionally cancel the notification
            context?.let {
                val notificationManager = NotificationManagerCompat.from(it)
                notificationManager.cancel(notificationId)
            }
        }
    }
}