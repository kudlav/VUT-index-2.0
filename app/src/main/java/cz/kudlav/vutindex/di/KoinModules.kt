package cz.kudlav.vutindex.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import cz.kudlav.vutindex.VutIndexNotificationManager
import cz.kudlav.vutindex.auth.viewmodel.LoginViewModel
import cz.kudlav.vutindex.home.viewmodel.HomeViewModel
import cz.kudlav.vutindex.menu.viewmodel.MenuViewModel
import cz.kudlav.vutindex.splash.viewmodel.SplashViewModel
import cz.kudlav.vutindex.system.viewmodel.SystemViewModel

val viewModelsModule = module {

    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MenuViewModel(get(), get(), get(), get()) }
    viewModel { SystemViewModel(get()) }
    viewModel { HomeViewModel() }

    single { VutIndexNotificationManager() }
}
