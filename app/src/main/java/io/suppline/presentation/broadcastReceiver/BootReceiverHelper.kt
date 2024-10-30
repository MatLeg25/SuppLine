package io.suppline.presentation.broadcastReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import dagger.hilt.android.AndroidEntryPoint
import io.suppline.domain.preferences.Preferences
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiverHelper @Inject constructor(
    private val preferences: Preferences
): BroadcastReceiver() {

    fun rescheduleAlarms(context: Context) {
        val s = preferences.loadDailySupplements()?.supplements
        println(">>>>>>>>> scheduleWorkManager , S = $s")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            System.currentTimeMillis() + 5000,
//            AlarmManager.INTERVAL_DAY,
//            alarmIntent
//        )
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        println(">>>>>>>>> scheduleWorkManager ,onReceive onReceive onReceive")
        TODO("Not yet implemented")
    }
}