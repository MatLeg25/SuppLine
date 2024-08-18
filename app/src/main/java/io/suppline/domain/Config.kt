package io.suppline.domain

import io.suppline.domain.models.Supplement

object Config {

    val DEFAULT_SUPPLEMENTS = listOf(
        Supplement(0, "AAA", false),
        Supplement(1, "BBB", false),
        Supplement(2, "CCC", false),
    )

}