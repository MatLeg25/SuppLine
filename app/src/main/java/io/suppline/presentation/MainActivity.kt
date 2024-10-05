package io.suppline.presentation

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import io.suppline.R
import io.suppline.presentation.broadcastReceiver.NotificationReceiver
import io.suppline.presentation.components.ActionButton
import io.suppline.presentation.components.AddEditItem
import io.suppline.presentation.components.DefaultSections
import io.suppline.presentation.components.GroupByTime
import io.suppline.presentation.components.Logo
import io.suppline.presentation.components.ProgressBar
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
        const val ACTION_NOTIFICATION = "ACTION_NOTIFICATION"
        const val EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID"
        const val EXTRA_NOTIFICATION_NAME = "EXTRA_NOTIFICATION_NAME"
        const val NOTIFICATION_RESPONSE = "NOTIFICATION_RESPONSE"
        const val EXTRA_RESPONSE_ACTION_INT = "EXTRA_RESPONSE_ACTION_INT"
    }

    private val viewModel: SuppLineViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.handleBroadcastAction(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        initPermissionLauncher()

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter(NOTIFICATION_RESPONSE))

        lifecycleScope.launch {
            viewModel.apply {
                updateNotification.collectLatest { notificationState ->
                    notificationState?.let {
                        if (notificationState.hasNotificationsPermission) {
                            notificationState.notification?.let {
                                if (it.active) scheduleNotification(
                                    context = this@MainActivity,
                                    notification = it,
                                    notificationState.isDailyNotification
                                )
                                else cancelScheduledNotification(
                                    context = this@MainActivity,
                                    notification = it
                                )
                            }
                        } else {
                            askNotificationsPermission()
                        }
                    }
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            SuppLineTheme {

                val state = viewModel.state.value

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Logo(modifier = Modifier)
                        //todo extract code to Screen composable fun
                        val isAddEditMode = (state.editedItem != null)
                        AnimatedVisibility(visible = isAddEditMode) {
                            AddEditItem(modifier = Modifier, viewModel = viewModel)
                        }
                        if (!isAddEditMode) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ActionButton(
                                    modifier = Modifier.padding(8.dp),
                                    text = stringResource(id = R.string.add_supplement),
                                    imageVector = Icons.Default.Add,
                                    onClick = { viewModel.setEditedItem() },
                                )
                            }
                        }
                        if (state.groupSectionsByTime) GroupByTime(
                            modifier = Modifier.weight(1f),
                            viewModel = viewModel,
                            hasNotificationsPermission = hasNotificationsPermission()
                        ) else DefaultSections(
                            modifier = Modifier.weight(1f),
                            supplements = state.supplements,
                            viewModel = viewModel,
                            hasNotificationsPermission = hasNotificationsPermission()
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        ProgressBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp),
                            progress = state.progress
                        )
                    }
                }

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
            }
        }
    }

    private fun hasNotificationsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED)
        } else {
            true
        }
    }

    private fun askNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun scheduleNotification(
        context: Context,
        notification: Notification,
        isDaily: Boolean
    ) {
        println(">>>>>>>>>>scheduleNotification: $notification : $isDaily ")
        val pendingIntent = getNotificationIntent(context, notification)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (isDaily) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                notification.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notification.timeInMillis,
                pendingIntent
            )
        }

    }

    private fun cancelScheduledNotification(context: Context, notification: Notification) {
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

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver when the activity is destroyed
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadcastReceiver)
    }

}


