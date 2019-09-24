package prvnimilion.vutindex.workers

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import timber.log.Timber
import java.util.concurrent.TimeUnit

class IndexWorkerManager {

    companion object {
        fun startIndexService(minutes: Long) {
            Timber.tag("VutIndexWorker").d("Worker has been scheduled for: $minutes minutes")
            val periodicWorkRequest =
                PeriodicWorkRequest.Builder(IndexWorker::class.java, minutes, TimeUnit.MINUTES)
                    .setConstraints(
                        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                    )
                    .build()
            WorkManager.getInstance().enqueue(periodicWorkRequest)
        }

        fun stopIndexService() {
            Timber.tag("VutIndexWorker").d("Worker has been laid off")
            WorkManager.getInstance().cancelAllWork()
        }
    }
}