package io.suppline.presentation.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import io.suppline.di.BootReceiverHelperEntryPoint

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println(">>>>>>>>>> BootRecive : onReceive")
        Toast.makeText(
            context,
            "Device Rebooted - BootBroadcastReceiver triggered",
            Toast.LENGTH_SHORT
        ).show()

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val helper = EntryPointAccessors.fromApplication(context, BootReceiverHelperEntryPoint::class.java)
            helper.getBootReceiverHelper().rescheduleAlarms(context)
        }
    }

}