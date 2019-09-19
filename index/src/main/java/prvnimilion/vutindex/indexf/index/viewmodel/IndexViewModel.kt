package prvnimilion.vutindex.indexf.index.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prvnimilion.vutindex.repository.repos.IndexRepository
import prvnimilion.vutindex.ui_common.models.Index

class IndexViewModel(private val repository: IndexRepository) : ViewModel() {

    val userIndex: MutableLiveData<Index> = MutableLiveData()

    fun getIndex() {
        viewModelScope.launch(Dispatchers.IO) {
            userIndex.postValue(repository.getIndex())
        }
    }
}
