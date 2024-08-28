package io.suppline.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.suppline.presentation.SuppLineViewModel

@Composable
fun GroupByTime(
    modifier: Modifier = Modifier,
    viewModel: SuppLineViewModel
) {
    val state = viewModel.state.value
    val sectionsMap = state.getSupplementsByHours()

    sectionsMap.forEach { (hour, supplements) ->
        Section(header = hour.toString()) {
            LazyColumn(modifier = modifier) {
                itemsIndexed(supplements) { index, item ->
                    if (index == 0) HorizontalDivider()
                    Supplement(
                        modifier = Modifier,
                        model = item,
                        isEditable = state.isEditMode,
                        onClick = { viewModel.toggleConsumed(item) },
                        toggleEditMode = { viewModel.toggleEditMode() },
                        setConsumedTime = viewModel::setConsumedTime,
                        setNotification = viewModel::setNotification
                    )
                    HorizontalDivider()
                }
            }
        }
    }

}