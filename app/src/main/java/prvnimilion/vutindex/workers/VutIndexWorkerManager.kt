package prvnimilion.vutindex.workers

import androidx.work.*
import prvnimilion.vutindex.BaseApplication
import timber.log.Timber
import java.util.concurrent.TimeUnit

class VutIndexWorkerManager {

    companion object {
        fun startServices(minutes: Long) {
            Timber.tag("VutIndexWorker").d("Worker has been scheduled for: $minutes minutes")
            val indexWorkRequest =
                PeriodicWorkRequest.Builder(IndexWorker::class.java, minutes, TimeUnit.MINUTES)
                    .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                    .build()
            WorkManager.getInstance(BaseApplication.applicationContext())
                .enqueueUniquePeriodicWork("VutIndexWorker-index", ExistingPeriodicWorkPolicy.KEEP, indexWorkRequest).result.toString()

            val messagesWorkRequest =
                PeriodicWorkRequest.Builder(MessageWorker::class.java, minutes, TimeUnit.MINUTES)
                    .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                    .build()
            WorkManager.getInstance(BaseApplication.applicationContext())
                .enqueueUniquePeriodicWork("VutIndexWorker-messages", ExistingPeriodicWorkPolicy.KEEP, messagesWorkRequest).result.toString()
        }

        fun stopServices() {
            Timber.tag("VutIndexWorker").d("Worker has been laid off")
            WorkManager.getInstance(BaseApplication.applicationContext()).cancelAllWork()
        }
    }
}