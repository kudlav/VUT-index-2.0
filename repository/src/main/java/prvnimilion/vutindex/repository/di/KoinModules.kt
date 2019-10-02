package prvnimilion.vutindex.repository.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import prvnimilion.vutindex.database.AppDatabase
import prvnimilion.vutindex.repository.repos.AuthRepository
import prvnimilion.vutindex.repository.repos.IndexRepository
import prvnimilion.vutindex.repository.repos.IsicCreditRepository
import prvnimilion.vutindex.repository.repos.MessagesRepository
import prvnimilion.vutindex.repository.security.PreferenceProvider

val repositoryModule = module {
    //repos
    single { AuthRepository(get(), get()) }
    single { IndexRepository(get(), AppDatabase.getInstance(androidContext()).indexDao()) }
    single { MessagesRepository(get(), get()) }
    single { IsicCreditRepository(get()) }

    //providers
    single { PreferenceProvider(get()) }
}
