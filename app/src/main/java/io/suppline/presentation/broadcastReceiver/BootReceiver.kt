package io.suppline.presentation.broadcastReceiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println(">>>>>>>>>> BootRecive : onReceive -> ${intent.action} ")
        Toast.makeText(
            context,
            "Device Rebooted - BootBroadcastReceiver triggered",
            Toast.LENGTH_SHORT
        ).show()

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            rescheduleAlarms(context)
        }
    }

    private fun rescheduleAlarms(context: Context) {
    println(">>>>>>>>> scheduleWorkManager ")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 5000,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }
}