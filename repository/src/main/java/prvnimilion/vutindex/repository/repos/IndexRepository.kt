package prvnimilion.vutindex.repository.repos

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import prvnimilion.vutindex.database.daos.IndexDao
import prvnimilion.vutindex.database.entities.SemesterEntity
import prvnimilion.vutindex.database.entities.SubjectEntity
import prvnimilion.vutindex.ui_common.enums.DifferenceType
import prvnimilion.vutindex.ui_common.models.Index
import prvnimilion.vutindex.ui_common.models.Semester
import prvnimilion.vutindex.ui_common.models.Subject
import prvnimilion.vutindex.ui_common.util.Difference
import prvnimilion.vutindex.webscraper.scrapers.IndexScraper
import timber.log.Timber

class IndexRepository(private val indexScraper: IndexScraper, private val indexDao: IndexDao) {

    suspend fun getIndex(): Index? {
        var index: Index? = null
        try {
            index = indexScraper.getIndex()
            saveIndexToDb(index)
        } catch (e: Exception) {
            Timber.e(e.toString())
        }
        return index
    }

    suspend fun getIndexFromDb(): Index? {
        val semesters = mutableListOf<Semester>()
        indexDao.getAllSemesters().forEach { semesterEntity ->

            val subjects = mutableListOf<Subject>()
            indexDao.getSubjectsBySemesterId(semesterEntity.semesterId).forEach {
                subjects.add(
                    Subject(
                        it.semesterId,
                        it.fullName,
                        it.shortName,
                        it.type,
                        it.credits,
                        it.completion,
                        it.creditGiven,
                        it.points,
                        it.grade,
                        it.termTime,
                        it.passed,
                        it.vsp
                    )
                )
            }
            semesters.add(
                Semester(
                    semesterEntity.semesterId,
                    semesterEntity.semesterHeader,
                    subjects
                )
            )
        }

        return Index(semesters)
    }

    private suspend fun saveIndexToDb(index: Index?) {
        if (index == null) return
        clearDb()

        index.semesters.forEach { semester ->
            var subjectCount = 0

            semester.subjects.forEach { subject ->

                indexDao.insert(
                    SubjectEntity(
                        0,
                        semester.semesterId,
                        subject.fullName,
                        subject.shortName,
                        subject.type,
                        subject.credits,
                        subject.completion,
                        subject.creditGiven,
                        subject.points,
                        subject.grade,
                        subject.termTime,
                        subject.passed,
                        subject.vsp
                    )
                )

                subjectCount++
            }

            indexDao.insert(SemesterEntity(0, semester.semesterId, semester.header, subjectCount))
        }
    }

    private suspend fun clearDb() {
        indexDao.deleteAllSubjects()
        indexDao.deleteAllSemesters()
    }

    suspend fun compareIndexes(coroutineScope: CoroutineScope): Difference? {
        var newIndex: Index? = null
        var oldIndex: Index? = null
        val job = coroutineScope.launch {
            newIndex = getIndex()
        }
        job.join()
        val job2 = coroutineScope.launch {
            oldIndex = getIndexFromDb()
        }
        job2.join()

        if (oldIndex == null || newIndex == null) return null

        if (oldIndex!!.semesters.size == newIndex!!.semesters.size) {
            for (i in 0 until oldIndex!!.semesters.size) {
                if (oldIndex!!.semesters[i] == newIndex!!.semesters[i]) {
                    for (j in 0 until oldIndex!!.semesters[i].subjects.size) {
                        val oldSubject = oldIndex!!.semesters[i].subjects[j]
                        val newSubject = newIndex!!.semesters[i].subjects[j]

                        when {
                            oldSubject.passed != newSubject.passed -> return Difference(
                                DifferenceType.PASSED,
                                newSubject.shortName,
                                passed = newSubject.passed
                            )
                            oldSubject.creditGiven != newSubject.creditGiven -> return Difference(
                                DifferenceType.CREDIT,
                                newSubject.shortName,
                                creditGiven = newSubject.creditGiven
                            )
                            oldSubject.points != newSubject.points -> return Difference(
                                DifferenceType.POINTS,
                                newSubject.shortName,
                                pointsGiven = (newSubject.points.toIntOrNull()?.minus(if (oldSubject.points.toIntOrNull() != null) oldSubject.points.toIntOrNull()!! else 0))
                            )
                        }
                    }
                }
            }
        }
        return null
    }
}