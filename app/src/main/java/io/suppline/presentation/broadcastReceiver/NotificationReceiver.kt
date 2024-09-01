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
import io.suppline.presentation.MainActivity.Companion.ACTION_CANCEL
import io.suppline.presentation.MainActivity.Companion.ACTION_DONE
import io.suppline.presentation.MainActivity.Companion.ACTION_SNOOZE
import io.suppline.presentation.MainActivity.Companion.EXTRA_NOTIFICATION_ID
import io.suppline.presentation.MainActivity.Companion.EXTRA_NOTIFICATION_NAME
import io.suppline.presentation.MainActivity.Companion.NOTIFICATION_ID

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println(">>>> Recive at: ${System.currentTimeMillis()} , action = ${intent?.action}")

        when (intent?.action) {

            MainActivity.ACTION_NOTIFICATION -> {
                val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
                val notificationName = intent.getStringExtra(EXTRA_NOTIFICATION_NAME) ?: ""
                context?.let {
                    showNotification(it, notificationId, notificationName)
                }
            }

            ACTION_SNOOZE -> {
                val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
                // Handle the snooze action
                Toast.makeText(
                    context,
                    "Snooze action triggered for notification ID: $notificationId",
                    Toast.LENGTH_SHORT
                ).show()

            }

            ACTION_DONE -> {
                val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
                // Handle the snooze action
                Toast.makeText(
                    context,
                    "DONE action triggered for notification ID: $notificationId",
                    Toast.LENGTH_SHORT
                ).show()

            }

            ACTION_CANCEL -> {
                val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
                // Handle the snooze action
                Toast.makeText(
                    context,
                    "Cancel action triggered for notification ID: $notificationId",
                    Toast.LENGTH_SHORT
                ).show()

                // Optionally cancel the notification
                context?.let {
                    val notificationManager = NotificationManagerCompat.from(it)
                    notificationManager.cancel(notificationId)
                }
            }

            else -> Unit
        }
    }

    private fun showNotification(context: Context, notificationId: Int, notificationName: String) {
        with(context) {
            val notificationManager = NotificationManagerCompat.from(context)

            val snoozePendingIntent: PendingIntent = getPendingIntent(
                context = this,
                requestCode = 0,
                intent = Intent(this, NotificationReceiver::class.java).apply {
                    action = ACTION_SNOOZE
                    putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                },
            )

            val donePendingIntent: PendingIntent = getPendingIntent(
                context = this,
                requestCode = 1,
                intent = Intent(this, NotificationReceiver::class.java).apply {
                    action = ACTION_DONE
                    putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                },
            )

            val cancelPendingIntent: PendingIntent = getPendingIntent(
                context = this,
                requestCode = 2,
                intent = Intent(this, NotificationReceiver::class.java).apply {
                    action = ACTION_CANCEL
                    putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                },
            )

            val builder = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.suppline_X, notificationId))
                .setContentText(getString(R.string.time_to_consume_X, notificationName))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .addAction(
                    R.drawable.ic_launcher_background, getString(R.string.snooze),
                    snoozePendingIntent
                )
                .addAction(
                    R.drawable.ic_launcher_background, getString(R.string.done),
                    donePendingIntent
                )
                .addAction(
                    R.drawable.ic_launcher_background, getString(R.string.cancel),
                    cancelPendingIntent
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

    private fun getPendingIntent(context: Context, requestCode: Int, intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


}