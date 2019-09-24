package prvnimilion.vutindex.index.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import prvnimilion.vutindex.index.viewmodel.IndexViewModel

val indexModule = module {

    viewModel { IndexViewModel(get()) }

}
