package cz.kudlav.vutindex.repository.repos

import cz.kudlav.vutindex.webscraper.scrapers.IsicCreditScraper

class IsicCreditRepository(private val isicCreditScraper: IsicCreditScraper) {

    fun getIsicCredit(): String? {
        return isicCreditScraper.checkIsicCredit()
    }
}