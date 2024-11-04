package io.suppline.presentation.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.EntryPointAccessors

class BootReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                BootReceiverEntryPoint::class.java
            )
            // Access the SharedPreferences instance
            val sharedPreferences = hiltEntryPoint.sharedPreferences()
            val notificationReceiver = NotificationReceiver()
            notificationReceiver.rescheduleAlarms(context, sharedPreferences)
        }
    }

}