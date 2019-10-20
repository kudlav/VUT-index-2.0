package prvnimilion.vutindex.menu.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prvnimilion.vutindex.repository.repos.AuthRepository
import prvnimilion.vutindex.repository.repos.IsicCreditRepository
import prvnimilion.vutindex.workers.VutIndexWorkerManager

class MenuViewModel(
    private val authRepository: AuthRepository,
    private val isicCreditRepository: IsicCreditRepository
) : ViewModel() {

    val userLoggedOut: MutableLiveData<Boolean> = MutableLiveData()
    val userCredit: MutableLiveData<String> = MutableLiveData()

    fun logoutUser() {
        VutIndexWorkerManager.stopServices()
        viewModelScope.launch(Dispatchers.IO) {
            userLoggedOut.postValue(authRepository.logoutUser())
        }
    }

    fun getIsicCredit() {
        viewModelScope.launch(Dispatchers.IO) {
            userCredit.postValue(isicCreditRepository.getIsicCredit())
        }
    }
}