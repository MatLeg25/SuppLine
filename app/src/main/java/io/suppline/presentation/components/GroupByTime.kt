package io.suppline.presentation.components

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
            DefaultSections(
                modifier = modifier,
                supplements = supplements,
                viewModel = viewModel,
            )
        }
    }

}