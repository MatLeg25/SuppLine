package io.suppline.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import io.suppline.R
import io.suppline.presentation.broadcastReceiver.NotificationReceiver
import io.suppline.presentation.broadcastReceiver.NotificationReceiverContract
import io.suppline.presentation.components.ActionButton
import io.suppline.presentation.components.AddEditSupplementModal
import io.suppline.presentation.components.DefaultSections
import io.suppline.presentation.components.GroupByTime
import io.suppline.presentation.components.Logo
import io.suppline.presentation.components.ProgressBar
import io.suppline.presentation.enums.ErrorType
import io.suppline.presentation.error.NotificationException
import io.suppline.presentation.models.Notification
import io.suppline.presentation.ui.theme.SuppLineTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val CHANNEL_ID = "DailySupplementationChanel"
        const val NOTIFICATION_ID = 1234
        const val BTN_ACTION_SNOOZE = "BTN_ACTION_SNOOZE"
        const val BTN_ACTION_DONE = "BTN_ACTION_DONE"
        const val BTN_ACTION_CANCEL = "BTN_ACTION_CANCEL"
        const val ACTION_SHOW_NOTIFICATION = "ACTION_NOTIFICATION"
        const val ACTION_NOTIFICATION_PRESSED = "ACTION_NOTIFICATION_PRESSED"
        const val EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID"
        const val EXTRA_PARCELABLE_NOTIFICATION = "EXTRA_PARCELABLE_NOTIFICATION"
        const val NOTIFICATION_RESPONSE = "NOTIFICATION_RESPONSE"
        const val EXTRA_RESPONSE_ACTION_INT = "EXTRA_RESPONSE_ACTION_INT"
    }

    private val viewModel: SuppLineViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val broadcastReceiver: NotificationReceiverContract = object : NotificationReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.handleBroadcastAction(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        initPermissionLauncher()
        askScheduleExactAlarmsPermission()

        viewModel.scrollToIndex(intent?.getIntExtra(EXTRA_NOTIFICATION_ID, -1) ?: -1)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(NOTIFICATION_RESPONSE))

        lifecycleScope.launch {
            viewModel.apply {
                updateNotification.collectLatest { notificationState ->
                    notificationState?.let {
                        if (viewModel.hasNotificationsPermission) notificationState.notification?.let {
                            setNotification(it)
                        } else checkNotificationsPermission()
                    }
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            SuppLineTheme {
                SuppLineScreen(modifier = Modifier, viewModel = viewModel)
            }
        }
    }

    private fun initPermissionLauncher() {
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(
                    this,
                    this.getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            } else checkNotificationsPermission()
        }
    }

    private fun setNotification(notification: Notification) {
        try {
            if (notification.active) broadcastReceiver.scheduleNotification(
                context = this@MainActivity,
                notification = notification
            )
            else broadcastReceiver.cancelScheduledNotification(
                context = this@MainActivity,
                notification = notification
            )
        } catch (e: NotificationException) {
            when (e.type) {
                ErrorType.NO_POST_NOTIFICATIONS_PERMISSION -> askNotificationsPermission()
                ErrorType.NO_SCHEDULE_EXACT_ALARM_PERMISSION -> askScheduleExactAlarmsPermission()
                else -> displayUnknownErrorToast()
            }
        } catch (e: Exception) {
            displayUnknownErrorToast()
        }
    }

    private fun checkNotificationsPermission() {
        try {
            viewModel.hasNotificationsPermission = broadcastReceiver.hasPermissions(this)
        } catch (e: NotificationException) {
            when (e.type) {
                ErrorType.NO_POST_NOTIFICATIONS_PERMISSION -> askNotificationsPermission()
                ErrorType.NO_SCHEDULE_EXACT_ALARM_PERMISSION -> askScheduleExactAlarmsPermission()
                else -> displayUnknownErrorToast()
            }
        } catch (e: Exception) {
            displayUnknownErrorToast()
        }
    }

    private fun askNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun askScheduleExactAlarmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver when the activity is destroyed
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadcastReceiver)
    }

    private fun displayUnknownErrorToast() {
        Toast.makeText(
            this,
            getString(R.string.unknown_error_try_again_later),
            Toast.LENGTH_SHORT
        ).show()
    }

}


