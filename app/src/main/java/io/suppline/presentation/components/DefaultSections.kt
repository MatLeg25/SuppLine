package io.suppline.presentation.components

import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.suppline.presentation.SuppLineViewModel

@Composable
fun DefaultSections(
    modifier: Modifier = Modifier,
    viewModel: SuppLineViewModel,
    setNotification: (context: Context, timeInMillis: Long, notificationId: Int) -> Unit
) {
    val state = viewModel.state.value

    LazyColumn(modifier = modifier) {
        itemsIndexed(state.supplements) { index, item ->
            if (index == 0) HorizontalDivider()
            Supplement(
                modifier = Modifier,
                model = item,
                isEditable = state.isEditMode,
                onClick = { viewModel.toggleConsumed(item) },
                toggleEditMode = { viewModel.toggleEditMode() },
                setConsumedTime = viewModel::setConsumedTime,
                setNotification = setNotification
            )
            HorizontalDivider()
        }
    }

}