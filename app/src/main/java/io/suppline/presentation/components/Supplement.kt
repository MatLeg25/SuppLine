package io.suppline.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.suppline.R
import io.suppline.domain.models.Supplement

@Composable
fun Supplement(
    modifier: Modifier = Modifier,
    model: Supplement,
    isEditable: Boolean,
    onClick: () -> Unit,
    toggleEditMode: () -> Unit,
    onEditClick: (supplement: Supplement) -> Unit,
    setConsumedTime: (supplement: Supplement, hourDelta: Int, minDelta: Int) -> Unit,
    setNotification: (hasPermission: Boolean, supplement: Supplement) -> Unit,
    hasNotificationsPermission: Boolean
) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(0.65f),
            text = model.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(0.05f))
        if (!isEditable) {
            SelectionButton(
                modifier = Modifier.weight(0.3f),
                isSelected = model.consumed,
                onClick = { onClick() }
            )
        }
        Spacer(modifier = Modifier.weight(0.05f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            ConsumeTime(
                modifier = Modifier.clickable {
                    toggleEditMode()
                },
                length = 100.dp,
                isEditable = isEditable,
                model = model,
                setConsumedTime = setConsumedTime
            )
            Column {
                SetNotificationBtn(
                    isEnabled = isEditable,
                    isNotificationActive = model.hasNotification,
                    setNotification = { setNotification(hasNotificationsPermission, model) }
                )
                if (isEditable) {
                    ActionButton(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.edit),
                        imageVector = Icons.Default.Edit,
                        onClick = { onEditClick(model) },
                    )
                }
            }
        }

    }
}