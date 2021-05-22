package cz.kudlav.vutindex.home.viewmodel

import androidx.lifecycle.ViewModel
import cz.kudlav.vutindex.workers.VutIndexWorkerManager

class HomeViewModel : ViewModel() {
    var tabLayoutInitialized = false

    fun setupWorkers(){
        VutIndexWorkerManager.startServices(15)
    }
}


