package io.suppline.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.suppline.R
import io.suppline.presentation.components.ActionButton
import io.suppline.presentation.components.AddEditSupplementModal
import io.suppline.presentation.components.DefaultSections
import io.suppline.presentation.components.GroupByTime
import io.suppline.presentation.components.Logo
import io.suppline.presentation.components.ProgressBar
import io.suppline.presentation.ui.theme.SuppLineTheme

@Composable
fun SuppLineScreen(
    modifier: Modifier = Modifier,
    viewModel: SuppLineViewModel
) {
    SuppLineTheme {

        val state = viewModel.state.value
        var showModal by remember {
            mutableStateOf(false)
        }
        showModal = (state.editedItem != null)

        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Logo(modifier = Modifier)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ActionButton(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(id = R.string.add_supplement),
                        imageVector = Icons.Default.AddCircle,
                        onClick = {
                            viewModel.setEditedItem()
                            showModal = true
                        },
                    )
                }
                if (state.groupSectionsByTime) GroupByTime(
                    modifier = Modifier.weight(1f),
                    viewModel = viewModel,
                    index = state.scrollToIndex ?: -1
                ) else DefaultSections(
                    modifier = Modifier.weight(1f),
                    supplements = state.supplements,
                    viewModel = viewModel,
                    index = state.scrollToIndex ?: -1
                )
                Spacer(modifier = Modifier.height(24.dp))
                ProgressBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    progress = state.progress
                )
            }
        }

        if (showModal) {
            AddEditSupplementModal(
                viewModel = viewModel,
                setShowStats = { showModal = it },
            )
        }

    }
}