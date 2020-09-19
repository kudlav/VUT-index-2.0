package prvnimilion.vutindex.repository.repos

import prvnimilion.vutindex.webscraper.scrapers.IsicCreditScraper

class IsicCreditRepository(private val isicCreditScraper: IsicCreditScraper) {

    fun getIsicCredit(): String {
        return isicCreditScraper.checkIsicCredit() ?: "error" // TODO unable to translate
    }
}