package prvnimilion.vutindex.home.viewmodel

import androidx.lifecycle.ViewModel
import prvnimilion.vutindex.workers.VutIndexWorkerManager

class HomeViewModel : ViewModel() {
    var tabLayoutInitialized = false

    fun setupIndexWorker(){
        stopIndexWorker()
        VutIndexWorkerManager.startIndexService(15)
    }

    fun stopIndexWorker() {
        VutIndexWorkerManager.stopIndexService()
    }
}


