package io.suppline.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.suppline.domain.models.Supplement
import io.suppline.presentation.SuppLineViewModel

@Composable
fun DefaultSections(
    modifier: Modifier = Modifier,
    supplements: List<Supplement>,
    viewModel: SuppLineViewModel
) {
    val state = viewModel.state.value

    LazyColumn(modifier = modifier) {
        itemsIndexed(supplements) { index, item ->
            if (index == 0) HorizontalDivider()
            val isEditable = (state.editedItemIndex == index)
            AnimatedVisibility(visible = isEditable) {
                HorizontalDivider(Modifier.height(24.dp).background(Color.Transparent))
            }
            Supplement(
                modifier = Modifier,
                model = item,
                isEditable = isEditable,
                onClick = { viewModel.toggleConsumed(item) },
                toggleEditMode = { viewModel.toggleEditMode(index) },
                setConsumedTime = viewModel::setConsumedTime,
                setNotification = viewModel::setNotification
            )
            HorizontalDivider()
            AnimatedVisibility(visible = isEditable) {
                HorizontalDivider(Modifier.height(24.dp).background(Color.Transparent))
            }
        }
    }

}