package cz.kudlav.vutindex.menu.viewmodel

import android.webkit.CookieManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import cz.kudlav.vutindex.repository.repos.AuthRepository
import cz.kudlav.vutindex.repository.repos.HealthDeclareRepository
import cz.kudlav.vutindex.repository.repos.IndexRepository
import cz.kudlav.vutindex.repository.repos.IsicCreditRepository
import cz.kudlav.vutindex.workers.VutIndexWorkerManager

class MenuViewModel(
    private val authRepository: AuthRepository,
    private val indexRepository: IndexRepository,
    private val isicCreditRepository: IsicCreditRepository,
    private val healthDeclareRepository: HealthDeclareRepository
) : ViewModel() {

    val userLoggedOut: MutableLiveData<Boolean> = MutableLiveData()
    val userCredit: MutableLiveData<String?> = MutableLiveData()
    val userHealth: MutableLiveData<String?> = MutableLiveData()
    val userHealthSignError: MutableLiveData<Boolean> = MutableLiveData()

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

    fun getHealthDeclarationState() {
        viewModelScope.launch(Dispatchers.IO) {
            userHealth.postValue(healthDeclareRepository.getDeclarationState())
        }
    }

    fun signHealthDeclaration() {
        viewModelScope.launch(Dispatchers.IO) {
            val result: String? = healthDeclareRepository.signDeclaration()
            if (result != null)
                userHealth.postValue(result)
            else
                userHealthSignError.postValue(true)
        }
    }
}
