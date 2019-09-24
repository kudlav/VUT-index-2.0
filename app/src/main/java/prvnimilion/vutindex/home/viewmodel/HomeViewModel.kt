package prvnimilion.vutindex.home.viewmodel

import androidx.lifecycle.ViewModel
import prvnimilion.vutindex.workers.IndexWorkerManager

class HomeViewModel : ViewModel() {
    var tabLayoutInitialized = false

    fun setupIndexWorker(){
        stopIndexWorker()
        IndexWorkerManager.startIndexService(15)
    }

    fun stopIndexWorker() {
        IndexWorkerManager.stopIndexService()
    }
}


