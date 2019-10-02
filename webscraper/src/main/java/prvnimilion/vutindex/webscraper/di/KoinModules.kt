package prvnimilion.vutindex.webscraper.di

import org.koin.dsl.module
import prvnimilion.vutindex.webscraper.scrapers.IndexScraper
import prvnimilion.vutindex.webscraper.scrapers.LoginScraper
import prvnimilion.vutindex.webscraper.scrapers.MessagesScraper
import prvnimilion.vutindex.webscraper.util.VutCookieStore

val scrapersModule = module {

    //scrapers
    single { LoginScraper(get()) }
    single { IndexScraper(get()) }
    single { MessagesScraper(get()) }

    //cookies
    single { VutCookieStore(get()) }
}
