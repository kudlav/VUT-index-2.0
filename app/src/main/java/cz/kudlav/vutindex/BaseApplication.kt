package cz.kudlav.vutindex

import android.app.Application
import android.content.Context
import android.webkit.WebView
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import cz.kudlav.vutindex.di.viewModelsModule
import cz.kudlav.vutindex.index.di.indexModule
import cz.kudlav.vutindex.webscraper.di.scrapersModule
import timber.log.Timber
import android.content.pm.ApplicationInfo
import cz.kudlav.vutindex.repository.di.repositoryModule

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        startKoin {
            androidContext(this@BaseApplication)
            modules(listOf(viewModelsModule, repositoryModule, scrapersModule, indexModule))
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    init {
        instance = this
    }

    companion object {
        private var instance: BaseApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}