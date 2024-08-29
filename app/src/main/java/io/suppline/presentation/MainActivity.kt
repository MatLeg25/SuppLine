package io.suppline.presentation

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.suppline.R
import io.suppline.presentation.broadcastReceiver.NotificationReceiver
import io.suppline.presentation.components.DefaultSections
import io.suppline.presentation.components.GroupByTime
import io.suppline.presentation.components.Logo
import io.suppline.presentation.components.ProgressBar
import io.suppline.presentation.ui.theme.SuppLineTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val CHANNEL_ID = "DailySupplementationChanel"
        const val NOTIFICATION_ID = 1234
        const val ACTION_SNOOZE = "snooze"
        const val ACTION_NOTIFICATION = "ACTION_NOTIFICATION"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    }

    private val viewModel: SuppLineViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        initPermissionLauncher()

        lifecycleScope.launch {
            viewModel.apply {
                updateNotification.collectLatest { notificationState ->
                    notificationState?.let {
                        if (notificationState.hasNotificationsPermission) {
                            notificationState.notification?.let {
                                if (it.active) scheduleNotification(
                                    context = this@MainActivity,
                                    timeInMillis = it.timeInMillis,
                                    notificationId = it.id
                                )
                                else cancelScheduledNotification(
                                    context = this@MainActivity,
                                    notificationId = it.id
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
                        if (state.groupSectionsByTime) GroupByTime(
                            modifier = Modifier.weight(1f),
                            viewModel = viewModel,
                            hasNotificationsPermission = hasNotificationsPermission()
                        )
                        else DefaultSections(
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
        timeInMillis: Long,
        notificationId: Int
    ) { //todo pass more data to notification
        val pendingIntent = getNotificationIntent(context, notificationId)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    private fun cancelScheduledNotification(context: Context, notificationId: Int) {
        // get the same intent that was used to schedule the notification
        val pendingIntent = getNotificationIntent(context, notificationId)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        // Cancel the notification if it has already been shown
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    private fun getNotificationIntent(context: Context, notificationId: Int): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_NOTIFICATION
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }

}


