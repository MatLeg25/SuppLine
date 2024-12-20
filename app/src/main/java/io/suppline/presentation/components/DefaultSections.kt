package io.suppline.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.suppline.domain.models.Supplement
import io.suppline.presentation.SuppLineViewModel
import kotlinx.coroutines.launch

@Composable
fun DefaultSections(
    modifier: Modifier = Modifier,
    supplements: List<Supplement>,
    viewModel: SuppLineViewModel,
    index: Int = -1,
) {
    val state = viewModel.state.value
    val isEditMode = (state.configItemIndex != null)
    //prevent item moving when user is editing timeConsumed
    val items = if (isEditMode) supplements else supplements.sortedBy { it.timeToConsume }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        if (index > -1) {
            coroutineScope.launch {
                listState.scrollToItem(index)
                viewModel.onScrollEnd()
            }
        }
    }

    LazyColumn(modifier = modifier, state = listState) {
        itemsIndexed(items) { index, item ->
            if (index == 0) HorizontalDivider()
            val isEditable = (state.configItemIndex == index)
            AnimatedVisibility(visible = isEditable) {
                HorizontalDivider(
                    Modifier
                        .height(24.dp)
                        .background(Color.Transparent)
                )
            }
            Supplement(
                modifier = Modifier,
                model = item,
                isEditable = isEditable,
                onClick = { viewModel.toggleConsumed(item) },
                onEditClick = viewModel::onEditClick,
                toggleEditMode = { viewModel.toggleEditMode(index) },
                setConsumedTime = viewModel::setConsumedTime,
                setNotification = viewModel::setNotification,
            )
            HorizontalDivider()
            AnimatedVisibility(visible = isEditable) {
                HorizontalDivider(
                    Modifier
                        .height(24.dp)
                        .background(Color.Transparent)
                )
            }
        }
    }

}