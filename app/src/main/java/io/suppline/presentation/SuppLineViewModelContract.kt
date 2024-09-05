package io.suppline.presentation

import io.suppline.domain.models.Supplement
import io.suppline.presentation.events.AddEditSupplementEvent

interface SuppLineViewModelContract {
    fun onEvent(event: AddEditSupplementEvent)
    fun getEditedItem(): Supplement?
}