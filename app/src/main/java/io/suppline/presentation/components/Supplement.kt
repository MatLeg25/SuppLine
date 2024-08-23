package io.suppline.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.suppline.domain.models.Supplement
import java.time.LocalTime

@Composable
fun Supplement(
    modifier: Modifier = Modifier,
    model: Supplement,
    isEditable: Boolean,
    onClick: () -> Unit,
    toggleEditMode: () -> Unit,
    setConsumedTime: (supplement: Supplement, hour: Int, min: Int) -> Unit
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
        SelectionButton(
            modifier = Modifier.weight(0.3f),
            isSelected = model.consumed,
            onClick = { onClick() }
        )
        Spacer(modifier = Modifier.weight(0.05f))
        ConsumeTime(
            modifier = Modifier.clickable {
                toggleEditMode()
            },
            length = 100.dp,
            isEditable = isEditable,
            model = model,
            setConsumedTime = setConsumedTime
        )
    }
}