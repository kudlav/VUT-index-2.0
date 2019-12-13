package prvnimilion.vutindex.ui_common.models

data class Subject(
    var semesterId: Int,
    var fullName: String,
    var shortName: String,
    var type: String,
    var credits: String,
    var completion: String,
    var creditGiven: Boolean,
    var points: String,
    var grade: String,
    var termTime: String,
    var passed: Boolean,
    var vsp: String
)