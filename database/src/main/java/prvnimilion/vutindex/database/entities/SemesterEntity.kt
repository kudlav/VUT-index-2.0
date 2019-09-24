package prvnimilion.vutindex.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "semesters")
data class SemesterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val semesterId: Int,
    val semesterHeader: String,
    val subjectCount: Int
)