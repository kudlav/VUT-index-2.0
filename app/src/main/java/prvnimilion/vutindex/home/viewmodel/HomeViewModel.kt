package prvnimilion.vutindex.home.viewmodel

import androidx.lifecycle.ViewModel
import prvnimilion.vutindex.workers.VutIndexWorkerManager

class HomeViewModel : ViewModel() {
    var tabLayoutInitialized = false

    fun setupWorkers(){
        stopWorkers()
        VutIndexWorkerManager.startServices(15)
    }

    private fun stopWorkers() {
        VutIndexWorkerManager.stopServices()
    }
}


