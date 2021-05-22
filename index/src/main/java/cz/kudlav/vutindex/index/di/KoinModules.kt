package cz.kudlav.vutindex.index.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import cz.kudlav.vutindex.index.viewmodel.IndexViewModel

val indexModule = module {

    viewModel { IndexViewModel(get()) }

}
