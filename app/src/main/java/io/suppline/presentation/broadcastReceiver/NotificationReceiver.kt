package io.suppline.presentation.broadcastReceiver

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.suppline.R
import io.suppline.presentation.MainActivity
import io.suppline.presentation.MainActivity.Companion.ACTION_NOTIFICATION
import io.suppline.presentation.MainActivity.Companion.BTN_ACTION_CANCEL
import io.suppline.presentation.MainActivity.Companion.BTN_ACTION_DONE
import io.suppline.presentation.MainActivity.Companion.BTN_ACTION_SNOOZE
import io.suppline.presentation.MainActivity.Companion.EXTRA_NOTIFICATION_ID
import io.suppline.presentation.MainActivity.Companion.EXTRA_NOTIFICATION_NAME
import io.suppline.presentation.MainActivity.Companion.EXTRA_RESPONSE_ACTION_INT
import io.suppline.presentation.MainActivity.Companion.NOTIFICATION_ID
import io.suppline.presentation.MainActivity.Companion.NOTIFICATION_RESPONSE
import io.suppline.presentation.enums.NotificationResponseAction
import io.suppline.presentation.models.Notification

open class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println(">>>> Recive at: ${System.currentTimeMillis()} , action = ${intent?.action}")

        val notificationId = intent?.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
        if (notificationId == null || notificationId == -1) return

        when (intent.action) {

            MainActivity.ACTION_NOTIFICATION -> {
                context?.let {
                    showNotification(
                        context = it,
                        notificationId = notificationId,
                        notificationName = intent.getStringExtra(EXTRA_NOTIFICATION_NAME) ?: ""
                    )
                }
            }

            BTN_ACTION_SNOOZE -> handleUserAction(
                context, notificationId, NotificationResponseAction.SNOOZE
            )

            BTN_ACTION_DONE -> handleUserAction(
                context, notificationId, NotificationResponseAction.DONE
            )

            BTN_ACTION_CANCEL -> handleUserAction(
                context, notificationId, NotificationResponseAction.CANCEL
            )

            else -> Unit
        }
    }

    fun scheduleNotification(
        context: Context,
        notification: Notification
    ) {
        val timeMillis = System.currentTimeMillis() + 10000
        println(">>>>>>>>>>scheduleNotification: $notification , $timeMillis ")
        val pendingIntent = getNotificationIntent(context, notification)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            notification.timeInMillis,
            pendingIntent
        )
    }

    fun cancelScheduledNotification(context: Context, notification: Notification) {
        // get the same intent that was used to schedule the notification
        val pendingIntent = getNotificationIntent(context, notification)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        // Cancel the notification if it has already been shown
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notification.id)
    }

    private fun getNotificationIntent(context: Context, notification: Notification): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_NOTIFICATION
            putExtra(EXTRA_NOTIFICATION_ID, notification.id)
            putExtra(EXTRA_NOTIFICATION_NAME, notification.name)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notification.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }

    private fun showNotification(context: Context, notificationId: Int, notificationName: String) {
        with(context) {
            val notificationManager = NotificationManagerCompat.from(context)

            val contentIntent: PendingIntent = PendingIntent.getActivity(
                this,
                123,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val snoozePendingIntent: PendingIntent = getPendingIntent(
                context = this,
                requestCode = NotificationResponseAction.SNOOZE.value,
                intent = Intent(this, NotificationReceiver::class.java).apply {
                    action = BTN_ACTION_SNOOZE
                    putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                },
            )

            val donePendingIntent: PendingIntent = getPendingIntent(
                context = this,
                requestCode = NotificationResponseAction.DONE.value,
                intent = Intent(this, NotificationReceiver::class.java).apply {
                    action = BTN_ACTION_DONE
                    putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                },
            )

            val cancelPendingIntent: PendingIntent = getPendingIntent(
                context = this,
                requestCode = NotificationResponseAction.CANCEL.value,
                intent = Intent(this, NotificationReceiver::class.java).apply {
                    action = BTN_ACTION_CANCEL
                    putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                },
            )

            val builder = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.suppline_X, notificationId))
                .setContentText(getString(R.string.time_to_consume_X, notificationName))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(contentIntent)
                .addAction(
                    R.drawable.ic_launcher_background,
                    getString(R.string.snooze),
                    snoozePendingIntent
                ).addAction(
                    R.drawable.ic_launcher_background, getString(R.string.done), donePendingIntent
                ).addAction(
                    R.drawable.ic_launcher_background,
                    getString(R.string.cancel),
                    cancelPendingIntent
                ).setAutoCancel(true)


            if (ActivityCompat.checkSelfPermission(
                    this@with, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }

            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun getPendingIntent(
        context: Context, requestCode: Int, intent: Intent
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun handleUserAction(
        context: Context?, notificationId: Int, responseAction: NotificationResponseAction
    ) {
        // Handle the snooze action
        Toast.makeText(
            context,
            "$responseAction action triggered for notification ID: $notificationId",
            Toast.LENGTH_SHORT
        ).show()
        context?.let {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.cancel(NOTIFICATION_ID)
            val localIntent = Intent(NOTIFICATION_RESPONSE).apply {
                putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                putExtra(EXTRA_RESPONSE_ACTION_INT, responseAction.value)
            }
            LocalBroadcastManager.getInstance(it).sendBroadcast(localIntent)
        }
    }


}