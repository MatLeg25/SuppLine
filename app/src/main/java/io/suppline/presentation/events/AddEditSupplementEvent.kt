package io.suppline.presentation.events

import io.suppline.domain.models.Supplement

sealed class AddEditSupplementEvent {
    data class OnNameChange(val supplement: Supplement,val name: String): AddEditSupplementEvent()
    data class OnDescriptionChange(val supplement: Supplement, val description: String): AddEditSupplementEvent()
}