package prvnimilion.vutindex.ui_common.models

data class Subject(
    val id: Int,
    val fullName: String,
    val shortName: String,
    val type: String,
    val credits: String,
    val completion: String,
    val creditGiven: Boolean,
    val points: String,
    val grade: String,
    val termTime: String,
    val passed: Boolean,
    val vsp: String
)