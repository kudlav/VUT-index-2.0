package prvnimilion.vutindex.repository.di

import org.koin.dsl.module
import prvnimilion.vutindex.repository.repos.AuthRepository
import prvnimilion.vutindex.repository.repos.IndexRepository
import prvnimilion.vutindex.repository.security.PreferenceProvider
import prvnimilion.vutindex.webscraper.scrapers.IndexScraper
import prvnimilion.vutindex.webscraper.scrapers.LoginScraper

val repositoryModule = module {

    //repos
    single { AuthRepository(LoginScraper(get()), get()) }
    single { IndexRepository(IndexScraper(get())) }

    //providers
    single { PreferenceProvider(get()) }
}
