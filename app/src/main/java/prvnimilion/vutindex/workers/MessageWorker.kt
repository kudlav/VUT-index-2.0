package prvnimilion.vutindex.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import prvnimilion.vutindex.VutIndexNotificationManager
import prvnimilion.vutindex.repository.repos.AuthRepository
import prvnimilion.vutindex.repository.repos.MessagesRepository
import prvnimilion.vutindex.ui_common.enums.DifferenceType
import prvnimilion.vutindex.ui_common.util.Difference
import timber.log.Timber

class MessageWorker(appContext: Context, workerParameters: WorkerParameters) : CoroutineWorker(
    appContext, workerParameters
), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val messagesRepository: MessagesRepository by inject()
    private val vutIndexNotificationManager: VutIndexNotificationManager by inject()


    override suspend fun doWork(): Result = coroutineScope {
        Timber.tag("VutIndexWorker").d("Messages Worker has started to work!")
        if (authRepository.quickLogin()) {
            if(messagesRepository.isNewMessage()){
                vutIndexNotificationManager.sendNotification(Difference(DifferenceType.MESSAGE))
            }
        }
        Result.success()
    }

}