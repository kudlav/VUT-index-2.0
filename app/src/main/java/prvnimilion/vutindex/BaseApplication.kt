package prvnimilion.vutindex

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import prvnimilion.vutindex.di.viewModelsModule
import prvnimilion.vutindex.indexf.di.indexModule
import prvnimilion.vutindex.repository.di.repositoryModule
import prvnimilion.vutindex.webscraper.di.scrapersModule
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(viewModelsModule, repositoryModule, scrapersModule, indexModule))
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}