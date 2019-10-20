package prvnimilion.vutindex.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import prvnimilion.vutindex.VutIndexNotificationManager
import prvnimilion.vutindex.auth.viewmodel.LoginViewModel
import prvnimilion.vutindex.home.viewmodel.HomeViewModel
import prvnimilion.vutindex.menu.viewmodel.MenuViewModel
import prvnimilion.vutindex.splash.viewmodel.SplashViewModel
import prvnimilion.vutindex.system.viewmodel.SystemViewModel

val viewModelsModule = module {

    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { MenuViewModel(get(), get(), get()) }
    viewModel { SystemViewModel(get()) }
    viewModel { HomeViewModel() }

    single { VutIndexNotificationManager() }
}
