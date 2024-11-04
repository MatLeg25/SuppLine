package io.suppline.presentation.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import dagger.hilt.android.EntryPointAccessors

class BootReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println(">>>>>>>>>> BootRecive : onReceive")
        Toast.makeText(
            context,
            "Device Rebooted - BootBroadcastReceiver triggered",
            Toast.LENGTH_SHORT
        ).show()

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val hiltEntryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                BootReceiverEntryPoint::class.java
            )

            // Access the SharedPreferences instance
            val sharedPreferences = hiltEntryPoint.sharedPreferences()
            println(">>>>>>>>>>>>> sharedPreferences = ${sharedPreferences.all} ")

            val notificationReceiver = NotificationReceiver()
            notificationReceiver.rescheduleAlarms(context, sharedPreferences)
        }
    }

}