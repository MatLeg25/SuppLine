package io.suppline.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.suppline.R
import io.suppline.domain.Config
import io.suppline.presentation.SuppLineViewModelContract
import io.suppline.presentation.events.AddEditSupplementEvent


@Composable
fun AddEditItem(
    modifier: Modifier = Modifier,
    viewModel: SuppLineViewModelContract
) {
    val supplement =  viewModel.getEditedItem() ?: return
    var isNameError by remember {
        mutableStateOf(false)
    }
    var isDescriptionError by remember {
        mutableStateOf(false)
    }
    var descriptionCharactersLeft by remember {
        mutableIntStateOf(Config.MAX_DESCRIPTION_LENGTH - supplement.description.length)
    }

    fun validateName() {
        isNameError = supplement.name.isBlank()
    }

    fun validateDescription() {
        // Calculate the number of newline characters
        val lines = supplement.description.count { it == '\n' }
        descriptionCharactersLeft = (Config.MAX_DESCRIPTION_LENGTH - supplement.description.length)
        isDescriptionError = (supplement.description.length > Config.MAX_DESCRIPTION_LENGTH) || lines >= Config.MAX_NEW_LINES
    }

    key(supplement) {
        validateName()
        validateDescription()
    }

    Column(modifier = modifier) {
        Row {
            Column {
                TextField(
                    label = {
                        Text(text = stringResource(id = R.string.name))
                    },
                    placeholder = {
                        Text(text = stringResource(id = if (isNameError) R.string.name_cannot_be_empty else R.string.name))
                    },
                    value = supplement.name,
                    onValueChange = {
                        viewModel.onEvent(
                            event = AddEditSupplementEvent.OnNameChange(
                                supplement = supplement,
                                name = it
                            )
                        )
                    },
                    isError = isNameError
                )

                TextField(
                    label = {
                        Text(text = stringResource(id = R.string.description) + " [$descriptionCharactersLeft]")
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.description))
                    },
                    value = supplement.description,
                    onValueChange = {
                        viewModel.onEvent(
                            event = AddEditSupplementEvent.OnDescriptionChange(
                                supplement = supplement,
                                description = it
                            )
                        )
                    },
                    isError = isDescriptionError
                )
            }
        }
        Row {
            ActionButton(
                modifier = Modifier.padding(8.dp),
                text = "", //stringResource(id = R.string.add),
                imageVector = Icons.Default.CheckCircle,
                onClick = {
                    validateName()
                    validateDescription()
                    if (!(isNameError || isDescriptionError)) {
                        viewModel.onEvent(
                            event = AddEditSupplementEvent.Accept
                        )
                    }
                },
            )
            ActionButton(
                modifier = Modifier.padding(8.dp),
                text = "", //stringResource(id = R.string.cancel),
                imageVector = Icons.Default.Clear,
                onClick = {
                    viewModel.onEvent(
                        event = AddEditSupplementEvent.Reject
                    )
                },
            )
            ActionButton(
                modifier = Modifier.padding(8.dp),
                text = "", //stringResource(id = R.string.remove),
                imageVector = Icons.Default.Delete,
                onClick = {
                    viewModel.onEvent(
                        event = AddEditSupplementEvent.OnRemove(supplement)
                    )
                },
            )
        }
    }
}