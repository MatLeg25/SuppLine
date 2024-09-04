package io.suppline.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.suppline.R
import io.suppline.presentation.SuppLineViewModelContract
import io.suppline.presentation.events.AddEditSupplementEvent

@Composable
fun AddEditItem(
    modifier: Modifier = Modifier,
    viewmodel: SuppLineViewModelContract
) {
    val supplement = viewmodel.getEditedItem()

    Row(modifier = modifier) {
        Column {
            TextField(
                label = {
                    Text(text = stringResource(id = R.string.name))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.name))
                },
                value = supplement.name,
                onValueChange = {
                    viewmodel.onEvent(
                        event = AddEditSupplementEvent.OnNameChange(
                            supplement = supplement,
                            name = it
                        )
                    )
                }
            )
            TextField(
                label = {
                    Text(text = stringResource(id = R.string.description))
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.description) + "(${stringResource(id = R.string.description)})")
                },
                value = supplement.description,
                onValueChange = {
                    viewmodel.onEvent(
                        event = AddEditSupplementEvent.OnDescriptionChange(
                            supplement = supplement,
                            description = it
                        )
                    )
                }
            )
        }

    }
}