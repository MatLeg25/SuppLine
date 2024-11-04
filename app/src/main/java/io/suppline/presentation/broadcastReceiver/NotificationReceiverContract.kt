package io.suppline.presentation.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import io.suppline.domain.preferences.Preferences
import io.suppline.presentation.error.NotificationException
import io.suppline.presentation.models.Notification

abstract class NotificationReceiverContract: BroadcastReceiver()  {
    @Throws(NotificationException::class)
    abstract fun scheduleNotification(context: Context, notification: Notification)

    @Throws(NotificationException::class)
    abstract fun cancelScheduledNotification(context: Context, notification: Notification)

    @Throws(NotificationException::class)
    abstract fun hasPermissions(context: Context): Boolean

    abstract fun rescheduleAlarms(context: Context, preferences: Preferences)
}