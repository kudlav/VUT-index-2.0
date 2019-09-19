package prvnimilion.vutindex.indexf.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import prvnimilion.vutindex.indexf.index.viewmodel.IndexViewModel

val indexModule = module {

    viewModel { IndexViewModel(get()) }

}
