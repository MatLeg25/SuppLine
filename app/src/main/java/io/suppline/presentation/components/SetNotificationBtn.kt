package io.suppline.presentation.components

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.suppline.R

@Composable
fun SetNotificationBtn(
    modifier: Modifier = Modifier,
    isNotificationActive: Boolean,
    notificationTimeInMillis: Long,
    notificationId: Int,
    setNotification: (context: Context, timeInMillis: Long, notificationId: Int) -> Unit
) {
    val context = LocalContext.current

    IconButton(
        modifier = modifier,
        onClick = { setNotification(context, notificationTimeInMillis, notificationId) }
    ) {
        Icon(
            imageVector = if (isNotificationActive) Icons.Filled.Notifications else Icons.Outlined.Notifications,
            contentDescription = stringResource(id = R.string.set_notification)
        )
    }
}