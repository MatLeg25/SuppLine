package io.suppline.domain

object Config {

    val DEFAULT_SUPPLEMENT_NAMES = listOf(
        "Ashwagandha",
        "Citrulline",
        "Creatine",
        "Electrolyte",
        "Omega 3",
        "Protein",
        "Siberian Ginseng",
        "Vitamin C",
    )

    const val SHARED_PREFERENCES_NAME = "SuppLine_shared_preferences"

    const val MAX_DESCRIPTION_LENGTH = 100
    const val MAX_NEW_LINES = 3

}