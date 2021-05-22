package cz.kudlav.vutindex.index.models

private const val NO_ID = -1

interface IndexFeedModel

fun IndexFeedModel.id(): Int {
    return when (this) {
        is IndexSubjectModel -> this.id
        is IndexHeaderModel -> this.id
        else -> NO_ID
    }
}