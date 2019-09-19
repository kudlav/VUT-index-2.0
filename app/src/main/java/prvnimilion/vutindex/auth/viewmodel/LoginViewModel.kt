package prvnimilion.vutindex.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prvnimilion.vutindex.repository.repos.AuthRepository

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    val userLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    val userCredentials: MutableLiveData<MutableMap<String, String>> = MutableLiveData()

    fun loginUser(username: String, password: String, saveCredentials: Boolean? = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.loginUser(username, password, saveCredentials)) {
                userLoggedIn.postValue(true)
            } else {
                //TODO: Resolve the reason
                userLoggedIn.postValue(false)
            }
        }
    }

    fun getCredentials() {
        viewModelScope.launch(Dispatchers.IO) {
            userCredentials.postValue(repository.getCredentials())
        }
    }
}
