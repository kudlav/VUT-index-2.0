package prvnimilion.vutindex.index.models

data class IndexSubjectModel(
    val id : Int,
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
) : IndexFeedModel