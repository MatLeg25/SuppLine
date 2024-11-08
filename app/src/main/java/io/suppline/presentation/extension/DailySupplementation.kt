package io.suppline.presentation.extension

import io.suppline.domain.models.Supplement

fun List<Supplement>.toggleConsumed(supplement: Supplement): List<Supplement> {
    val list = this.toMutableList()
    val indexToReplace = list.indexOf(supplement)
    if (indexToReplace in 0..list.lastIndex) {
        list[indexToReplace] = supplement.copy(consumed = !supplement.consumed)
    }
    return list.toList()
}