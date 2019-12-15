package prvnimilion.vutindex.workers

import androidx.work.*
import prvnimilion.vutindex.BaseApplication
import timber.log.Timber
import java.util.concurrent.TimeUnit

class VutIndexWorkerManager {

    companion object {
        fun startServices(minutes: Long) {
            Timber.tag("VutIndexWorker").d("Worker has been scheduled for: $minutes minutes")
            val notificationWorkRequest =
                PeriodicWorkRequest.Builder(
                    NotificationWorker::class.java,
                    minutes,
                    TimeUnit.MINUTES
                )
                    .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                    .build()
            WorkManager.getInstance(BaseApplication.applicationContext())
                .enqueueUniquePeriodicWork(
                    "VutIndexWorker-notifications",
                    ExistingPeriodicWorkPolicy.KEEP,
                    notificationWorkRequest
                ).result.toString()

        }

        fun stopServices() {
            Timber.tag("VutIndexWorker").d("Worker has been laid off")
            WorkManager.getInstance(BaseApplication.applicationContext()).cancelAllWork()
        }
    }
}