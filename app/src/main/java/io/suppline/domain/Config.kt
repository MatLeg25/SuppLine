package io.suppline.domain

import io.suppline.domain.models.Supplement

object Config {

    val DEFAULT_SUPPLEMENTS = listOf(
        Supplement(0, "Ashwagandha"),
        Supplement(1, "Citrulline"),
        Supplement(2, "Creatine"),
        Supplement(3, "Electrolyte"),
        Supplement(4, "Omega 3"),
        Supplement(5, "Protein"),
        Supplement(6, "Siberian Ginseng"),
        Supplement(7, "Vitamin C"),
    )

}