package io.suppline.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.suppline.R

@Composable
fun SetNotificationBtn(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    isNotificationActive: Boolean,
    setNotification: () -> Unit
) {
    IconButton(
        modifier = modifier,
        enabled = isEnabled,
        onClick = { setNotification() }
    ) {
        Icon(
            imageVector = if (isNotificationActive) Icons.Filled.Notifications else Icons.Outlined.Notifications,
            contentDescription = stringResource(id = R.string.set_notification)
        )
    }
}