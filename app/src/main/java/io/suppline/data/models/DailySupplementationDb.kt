package io.suppline.data.models

data class DailySupplementationDb(
    val date: Long,
    val supplements: List<SupplementDb>
)