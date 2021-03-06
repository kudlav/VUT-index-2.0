package cz.kudlav.vutindex.splash.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import cz.kudlav.vutindex.repository.repos.AuthRepository

class SplashViewModel(private val repository: AuthRepository) : ViewModel() {

    val userLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun tryQuickLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            userLoggedIn.postValue(repository.quickLogin())
        }
    }

}
