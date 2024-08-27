package io.suppline.presentation.broadcastReceiver

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.suppline.R
import io.suppline.presentation.MainActivity
import io.suppline.presentation.MainActivity.Companion.ACTION_SNOOZE
import io.suppline.presentation.MainActivity.Companion.EXTRA_NOTIFICATION_ID
import io.suppline.presentation.MainActivity.Companion.NOTIFICATION_ID

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println(">>>> Recive at: ${System.currentTimeMillis()} , action = ${intent?.action}")

        when (intent?.action) {
            ACTION_SNOOZE -> {
                val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
                // Handle the snooze action
                Toast.makeText(
                    context,
                    "Snooze action triggered for notification ID: $notificationId",
                    Toast.LENGTH_SHORT
                ).show()

                // Optionally cancel the notification
                context?.let {
                    val notificationManager = NotificationManagerCompat.from(it)
                    notificationManager.cancel(notificationId)
                }
            }

            MainActivity.ACTION_NOTIFICATION -> {
                val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
                context?.let {
                    showNotification(it, notificationId)
                }
            }

            else -> Unit
        }
    }

    private fun showNotification(context: Context, notificationId: Int) {
        with(context) {
            val notificationManager = NotificationManagerCompat.from(context)
            val snoozeIntent = Intent(this, NotificationReceiver::class.java).apply {
                action = ACTION_SNOOZE
                putExtra(EXTRA_NOTIFICATION_ID, 0)
            }
            val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification id: $notificationId")
                .setContentText("Time to consume item with ID=$notificationId")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(snoozePendingIntent)
                .addAction(
                    R.drawable.ic_launcher_background, getString(R.string.snooze),
                    snoozePendingIntent
                )
                .setAutoCancel(true)


            if (ActivityCompat.checkSelfPermission(
                    this@with,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }


}