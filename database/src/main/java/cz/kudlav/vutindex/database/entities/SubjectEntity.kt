package cz.kudlav.vutindex.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")

data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val semesterId: Int,
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