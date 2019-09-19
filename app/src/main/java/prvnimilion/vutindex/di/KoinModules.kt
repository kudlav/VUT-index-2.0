package prvnimilion.vutindex.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import prvnimilion.vutindex.auth.viewmodel.LoginViewModel
import prvnimilion.vutindex.home.viewmodel.HomeNavViewModel
import prvnimilion.vutindex.splash.viewmodel.SplashViewModel

val viewModelsModule = module {

    viewModel { SplashViewModel() }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeNavViewModel() }

}
