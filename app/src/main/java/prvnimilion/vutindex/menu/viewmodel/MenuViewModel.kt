package prvnimilion.vutindex.menu.viewmodel

import android.webkit.CookieManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prvnimilion.vutindex.repository.repos.AuthRepository
import prvnimilion.vutindex.repository.repos.IndexRepository
import prvnimilion.vutindex.repository.repos.IsicCreditRepository
import prvnimilion.vutindex.workers.VutIndexWorkerManager

class MenuViewModel(
    private val authRepository: AuthRepository,
    private val indexRepository: IndexRepository,
    private val isicCreditRepository: IsicCreditRepository
) : ViewModel() {

    val userLoggedOut: MutableLiveData<Boolean> = MutableLiveData()
    val userCredit: MutableLiveData<String> = MutableLiveData()

    fun logoutUser() {
        VutIndexWorkerManager.stopServices()
        viewModelScope.launch(Dispatchers.IO) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
            indexRepository.clearDb()
            userLoggedOut.postValue(authRepository.logoutUser())
        }
    }

    fun getIsicCredit() {
        viewModelScope.launch(Dispatchers.IO) {
            userCredit.postValue(isicCreditRepository.getIsicCredit())
        }
    }
}