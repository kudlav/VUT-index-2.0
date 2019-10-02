package prvnimilion.vutindex.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import prvnimilion.vutindex.VutIndexNotificationManager
import prvnimilion.vutindex.repository.repos.AuthRepository
import prvnimilion.vutindex.repository.repos.IndexRepository
import timber.log.Timber

class IndexWorker(appContext: Context, workerParameters: WorkerParameters) : CoroutineWorker(
    appContext, workerParameters
), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val indexRepository: IndexRepository by inject()
    private val vutIndexNotificationManager: VutIndexNotificationManager by inject()


    override suspend fun doWork(): Result = coroutineScope {
        Timber.tag("VutIndexWorker").d("Index Worker has started to work!")
        if (authRepository.quickLogin()) {
            vutIndexNotificationManager.sendNotification(indexRepository.compareIndexes(this))
        }
        Result.success()
    }
}