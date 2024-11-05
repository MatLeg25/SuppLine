package io.suppline.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.suppline.R
import io.suppline.domain.Config
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
    setNotification: (supplement: Supplement) -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(0.65f)) {
            Text(
                modifier = Modifier,
                fontSize = 16.sp,
                text = model.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (model.description.isNotBlank()) {
                Text(
                    modifier = Modifier,
                    fontSize = 8.sp,
                    fontStyle = FontStyle.Italic,
                    text = model.description,
                    maxLines = Config.MAX_NEW_LINES,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
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
                    setNotification = { setNotification(model) }
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