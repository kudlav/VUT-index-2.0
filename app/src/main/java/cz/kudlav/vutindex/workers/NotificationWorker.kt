package cz.kudlav.vutindex.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import cz.kudlav.vutindex.VutIndexNotificationManager
import cz.kudlav.vutindex.repository.repos.AuthRepository
import cz.kudlav.vutindex.repository.repos.IndexRepository
import cz.kudlav.vutindex.repository.repos.MessagesRepository
import cz.kudlav.vutindex.ui_common.enums.DifferenceType
import cz.kudlav.vutindex.ui_common.util.Difference
import timber.log.Timber

class NotificationWorker(appContext: Context, workerParameters: WorkerParameters) : CoroutineWorker(
    appContext, workerParameters
), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val indexRepository: IndexRepository by inject()
    private val messagesRepository: MessagesRepository by inject()
    private val vutIndexNotificationManager: VutIndexNotificationManager by inject()

    override suspend fun doWork(): Result = coroutineScope {
        Timber.tag("VutIndexWorker").d("Notification Worker has started to work!")
        if (authRepository.quickLogin()) {
            vutIndexNotificationManager.sendNotification(indexRepository.compareIndexes(this))
            if(messagesRepository.isNewMessage()){
                vutIndexNotificationManager.sendNotification(Difference(DifferenceType.MESSAGE))
            }
        }
        Result.success()
    }
}