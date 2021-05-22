package cz.kudlav.vutindex.ui_common.util

import cz.kudlav.vutindex.ui_common.enums.DifferenceType

data class Difference(
    val differenceType: DifferenceType,
    val subject: String? = null,
    val creditGiven: Boolean? = null,
    val pointsGiven: Int? = null,
    val passed: Boolean? = null
)