package cz.kudlav.vutindex.index.models

data class IndexSubjectModel(
    var id : Int,
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
) : IndexFeedModel