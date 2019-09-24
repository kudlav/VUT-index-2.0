package prvnimilion.vutindex.database.daos

import androidx.room.*
import prvnimilion.vutindex.database.entities.SemesterEntity
import prvnimilion.vutindex.database.entities.SubjectEntity

@Dao
interface IndexDao {

    @Query("SELECT * FROM subjects")
    suspend fun getAllSubjects(): MutableList<SubjectEntity>

    @Query("SELECT * FROM semesters")
    suspend fun getAllSemesters(): MutableList<SemesterEntity>

    @Query("SELECT * FROM subjects WHERE semesterId = :semesterId")
    suspend fun getSubjectsBySemesterId(semesterId: Int): MutableList<SubjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(u: SubjectEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(u: SemesterEntity): Long

    @Query(value = "DELETE FROM subjects")
    suspend fun deleteAllSubjects()

    @Query(value = "DELETE FROM semesters")
    suspend fun deleteAllSemesters()

}