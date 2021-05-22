package cz.kudlav.vutindex.webscraper.di

import org.koin.dsl.module
import cz.kudlav.vutindex.webscraper.scrapers.*
import cz.kudlav.vutindex.webscraper.util.VutCookieStore

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
