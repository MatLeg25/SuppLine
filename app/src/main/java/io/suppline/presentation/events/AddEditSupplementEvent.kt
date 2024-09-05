package io.suppline.presentation.events

import io.suppline.domain.models.Supplement

sealed class AddEditSupplementEvent {
    data object Accept: AddEditSupplementEvent()
    data object Reject: AddEditSupplementEvent()
    data class OnRemove(val supplement: Supplement): AddEditSupplementEvent()
    data class OnNameChange(val supplement: Supplement,val name: String): AddEditSupplementEvent()
    data class OnDescriptionChange(val supplement: Supplement, val description: String): AddEditSupplementEvent()
}