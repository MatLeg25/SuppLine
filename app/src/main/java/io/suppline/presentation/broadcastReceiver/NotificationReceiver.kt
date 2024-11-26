package io.suppline.presentation.broadcastReceiver

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.EntryPointAccessors
import io.suppline.R
import io.suppline.domain.models.DailySupplementation
import io.suppline.domain.preferences.Preferences
import io.suppline.domain.useCase.SaveDailySupplementationUseCase
import io.suppline.domain.utils.extensions.localTimeToEpochMillis
import io.suppline.presentation.MainActivity
import io.suppline.presentation.MainActivity.Companion.ACTION_NOTIFICATION_PRESSED
import io.suppline.presentation.MainActivity.Companion.ACTION_SHOW_NOTIFICATION
import io.suppline.presentation.MainActivity.Companion.BTN_ACTION_CANCEL
import io.suppline.presentation.MainActivity.Companion.BTN_ACTION_DONE
import io.suppline.presentation.MainActivity.Companion.BTN_ACTION_SNOOZE
import io.suppline.presentation.MainActivity.Companion.EXTRA_NOTIFICATION_ID
import io.suppline.presentation.MainActivity.Companion.EXTRA_PARCELABLE_NOTIFICATION
import io.suppline.presentation.MainActivity.Companion.EXTRA_RESPONSE_ACTION_INT
import io.suppline.presentation.MainActivity.Companion.NOTIFICATION_ID
import io.suppline.presentation.MainActivity.Companion.NOTIFICATION_RESPONSE
import io.suppline.presentation.enums.ErrorType
import io.suppline.presentation.enums.NotificationResponseAction
import io.suppline.presentation.error.NotificationException
import io.suppline.presentation.extension.parcelable
import io.suppline.presentation.extension.toggleConsumed
import io.suppline.presentation.mapper.toDomainModel
import io.suppline.presentation.mapper.toParcelable
import io.suppline.presentation.models.Notification
import io.suppline.presentation.models.ParcelableNotification
import kotlin.time.Duration.Companion.hours

open class NotificationReceiver : NotificationReceiverContract() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notification =
            intent?.parcelable<ParcelableNotification>(EXTRA_PARCELABLE_NOTIFICATION) ?: return
        when (intent.action) {

            ACTION_SHOW_NOTIFICATION -> {
                context?.let {
                    showNotification(context = it, notification = notification)
                    setForNextDay(context = it, notification = notification)
                }
            }

            BTN_ACTION_SNOOZE -> {
                context?.let {
                    //set notification (with original time) for next day
                    setForNextDay(context = it, notification = notification)
                    //set delayed notification (+15min) for today only
                    handleUserAction(
                        context, notification.id, NotificationResponseAction.SNOOZE
                    )
                }
            }

            BTN_ACTION_DONE -> handleUserAction(
                context, notification.id, NotificationResponseAction.DONE
            )

            BTN_ACTION_CANCEL -> handleUserAction(
                context, notification.id, NotificationResponseAction.CANCEL
            )

            else -> Unit
        }
    }

    @Throws(NotificationException::class)
    override fun scheduleNotification(
        context: Context, notification: Notification
    ) {
        if (hasPermissions(context)) {
            val pendingIntent = getNotificationIntent(context, notification)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notification.timeInMillis,
                pendingIntent
            )
        } else throw NotificationException(ErrorType.UNKNOWN_ERROR, "UNKNOWN_ERROR")
    }

    @Throws(NotificationException::class)
    override fun cancelScheduledNotification(context: Context, notification: Notification) {
        if (hasPermissions(context)) {
            // get the same intent that was used to schedule the notification
            val pendingIntent = getNotificationIntent(context, notification)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            // Cancel the notification if it has already been shown
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notification.id)
        } else throw NotificationException(ErrorType.UNKNOWN_ERROR, "UNKNOWN_ERROR")
    }

    @Throws(NotificationException::class)
    override fun hasPermissions(context: Context): Boolean {
        return hasNotificationsPermission(context) && hasScheduleExactAlarmsPermission(context)
    }

    private fun setForNextDay(notification: ParcelableNotification, context: Context) {
        if (notification.isDaily) {
            scheduleNotification(
                context = context,
                notification = notification.toDomainModel().copy(
                    timeInMillis = notification.timeInMillis.plus(24.hours.inWholeMilliseconds)
                )
            )
        }
    }

    private fun hasNotificationsPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (hasPermission) true else throw NotificationException(
                ErrorType.NO_POST_NOTIFICATIONS_PERMISSION, "NO_POST_NOTIFICATIONS_PERMISSION"
            )
        } else {
            true
        }
    }

    private fun hasScheduleExactAlarmsPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (alarmManager.canScheduleExactAlarms()) true else throw NotificationException(
                ErrorType.NO_SCHEDULE_EXACT_ALARM_PERMISSION, "NO_SCHEDULE_EXACT_ALARM_PERMISSION"
            )
        } else true
    }

    private fun getNotificationIntent(context: Context, notification: Notification): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_SHOW_NOTIFICATION
            putExtra(EXTRA_PARCELABLE_NOTIFICATION, notification.toParcelable())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notification.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }

    private fun showNotification(context: Context, notification: ParcelableNotification) {
        with(context) {
            val notificationManager = NotificationManagerCompat.from(context)

            val contentIntent: PendingIntent = PendingIntent.getActivity(
                this,
                123,
                Intent(this, MainActivity::class.java).apply {
                    action = ACTION_NOTIFICATION_PRESSED
                    putExtra(EXTRA_NOTIFICATION_ID, notification.id)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val snoozePendingIntent: PendingIntent = getPendingIntent(
                context = this,
                requestCode = NotificationResponseAction.SNOOZE.value,
                intent = Intent(this, NotificationReceiver::class.java).apply {
                    action = BTN_ACTION_SNOOZE
                    putExtra(EXTRA_PARCELABLE_NOTIFICATION, notification)
                },
            )

            val donePendingIntent: PendingIntent = getPendingIntent(
                context = this,
                requestCode = NotificationResponseAction.DONE.value,
                intent = Intent(this, NotificationReceiver::class.java).apply {
                    action = BTN_ACTION_DONE
                    putExtra(EXTRA_PARCELABLE_NOTIFICATION, notification)
                },
            )

            val cancelPendingIntent: PendingIntent = getPendingIntent(
                context = this,
                requestCode = NotificationResponseAction.CANCEL.value,
                intent = Intent(this, NotificationReceiver::class.java).apply {
                    action = BTN_ACTION_CANCEL
                    putExtra(EXTRA_PARCELABLE_NOTIFICATION, notification)
                },
            )

            val builder = NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.suppline_X, notification.id))
                .setContentText(getString(R.string.time_to_consume_X, notification.name))
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
        context?.let {
            handleActionInBackground(context, notificationId, responseAction)
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.cancel(NOTIFICATION_ID)
            val localIntent = Intent(NOTIFICATION_RESPONSE).apply {
                putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                putExtra(EXTRA_RESPONSE_ACTION_INT, responseAction.value)
            }
            LocalBroadcastManager.getInstance(it).sendBroadcast(localIntent)
        }
    }

    override fun rescheduleAlarms(context: Context, preferences: Preferences) {
        val alarms = preferences.loadDailySupplements()?.supplements?.filter { it.hasNotification }
        try {
            alarms?.forEach { supplement ->
                scheduleNotification(
                    context = context,
                    notification = Notification(
                        id = supplement.id,
                        name = supplement.name,
                        timeInMillis = supplement.timeToConsume.localTimeToEpochMillis(),
                        active = true,
                        isDaily = true
                    )
                )
            }
        } catch (e: Exception) {
            println(">>>> Cannot reschedule notification: ${e.message}")
        }
    }

    private fun handleActionInBackground(
        context: Context,
        notificationId: Int,
        responseAction: NotificationResponseAction
    ) {
        val dailySupplementation = getGetDailySupplementation(context) ?: return
        val supplement = dailySupplementation.supplements.find { it.id == notificationId } ?: return

        when (responseAction) {
            NotificationResponseAction.SNOOZE -> {
                try {
                    scheduleNotification(
                        context = context,
                        notification = Notification(
                            id = supplement.id,
                            name = supplement.name,
                            timeInMillis = supplement.timeToConsume.plusMinutes(15)
                                .localTimeToEpochMillis(),
                            active = true,
                            isDaily = false
                        )
                    )
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.permission_denied_app_in_background),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            NotificationResponseAction.DONE -> {
                val saveDailySupplementationUseCase = getSaveDailySupplementationUseCase(context)
                saveDailySupplementationUseCase.invoke(
                    dailySupplementation.copy(
                        supplements = dailySupplementation.supplements.toggleConsumed(supplement)
                    )
                )
            }

            NotificationResponseAction.CANCEL -> Unit
        }
    }

    private fun getGetDailySupplementation(context: Context): DailySupplementation? {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            GetDailySupplementationUseCaseEntryPoint::class.java
        )
        return hiltEntryPoint.getDailySupplementationUseCase().invoke()
    }

    private fun getSaveDailySupplementationUseCase(context: Context): SaveDailySupplementationUseCase {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            SaveDailySupplementationUseCaseEntryPoint::class.java
        )
        return hiltEntryPoint.getSaveDailySupplementationUseCase()
    }

}