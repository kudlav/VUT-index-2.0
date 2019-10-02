package prvnimilion.vutindex.menu.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prvnimilion.vutindex.repository.repos.AuthRepository
import prvnimilion.vutindex.workers.VutIndexWorkerManager

class MenuViewModel(private val authRepository: AuthRepository) : ViewModel() {

    val userLoggedOut: MutableLiveData<Boolean> = MutableLiveData()

    fun logoutUser() {
        VutIndexWorkerManager.stopIndexService()
        viewModelScope.launch(Dispatchers.IO) {
            userLoggedOut.postValue(authRepository.logoutUser())
        }
    }
}