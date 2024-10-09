package io.suppline.presentation.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableNotification(
    val id: Int,
    val name: String,
    val timeInMillis: Long,
    val active: Boolean,
    val isDaily: Boolean
) : Parcelable
