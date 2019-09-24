package prvnimilion.vutindex.index.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prvnimilion.vutindex.index.models.IndexFeedModel
import prvnimilion.vutindex.index.models.IndexHeaderModel
import prvnimilion.vutindex.index.models.IndexSubjectModel
import prvnimilion.vutindex.repository.repos.IndexRepository
import prvnimilion.vutindex.ui_common.models.Semester
import timber.log.Timber

class IndexViewModel(private val repository: IndexRepository) : ViewModel() {

    val dataSet = MutableLiveData<MutableList<IndexFeedModel>>()

    fun getIndex(networkOnly: Boolean? = false) {
        Timber.tag("__").d("Fetch index!")

        if (networkOnly == false) {
            //Database call
            viewModelScope.launch(Dispatchers.IO) {
                val index = repository.getIndexFromDb()
                val semesterData = mutableListOf<IndexFeedModel>()
                index?.semesters?.forEach { semester ->
                    semesterData.addAll(convertSemesterToFeedModel(semester))
                }
                if (semesterData.size > 0) {
                    dataSet.postValue(semesterData)
                }
            }
        }


        //Online call
        viewModelScope.launch(Dispatchers.IO) {
            val index = repository.getIndex()
            val semesterData = mutableListOf<IndexFeedModel>()
            index?.semesters?.forEach { semester ->
                semesterData.addAll(convertSemesterToFeedModel(semester))
            }
            if (semesterData.size > 0) {
                dataSet.postValue(semesterData)
            }
        }
    }

    private fun convertSemesterToFeedModel(semester: Semester): MutableList<IndexFeedModel> {
        var id = 0
        val semesterData = mutableListOf<IndexFeedModel>()
        semesterData.add(
            IndexHeaderModel(
                id++,
                semester.header
            )
        )
        semester.subjects.forEach { sub ->
            semesterData.add(
                IndexSubjectModel(
                    id++,
                    sub.fullName,
                    sub.shortName,
                    sub.type,
                    sub.credits,
                    sub.completion,
                    sub.creditGiven,
                    sub.points,
                    sub.grade,
                    sub.termTime,
                    sub.passed,
                    sub.vsp
                )
            )
        }

        return semesterData
    }


}
