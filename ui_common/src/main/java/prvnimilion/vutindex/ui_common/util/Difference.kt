package prvnimilion.vutindex.ui_common.util

import prvnimilion.vutindex.ui_common.enums.DifferenceType

data class Difference(
    val differenceType: DifferenceType,
    val subject: String? = null,
    val creditGiven: Boolean? = null,
    val pointsGiven: Int? = null,
    val passed: Boolean? = null
)