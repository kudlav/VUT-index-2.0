package prvnimilion.vutindex.webscraper.di

import org.koin.dsl.module
import prvnimilion.vutindex.webscraper.scrapers.*
import prvnimilion.vutindex.webscraper.util.VutCookieStore

val scrapersModule = module {

    //scrapers
    single { LoginScraper(get()) }
    single { IndexScraper(get()) }
    single { MessagesScraper(get()) }
    single { IsicCreditScraper(get()) }
    single { HealthDeclareScraper(get()) }

    //cookies
    single { VutCookieStore(get()) }
}
