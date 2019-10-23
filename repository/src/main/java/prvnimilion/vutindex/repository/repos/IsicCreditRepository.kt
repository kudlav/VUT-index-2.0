package prvnimilion.vutindex.repository.repos

import prvnimilion.vutindex.webscraper.scrapers.IsicCreditScraper
import timber.log.Timber

class IsicCreditRepository(private val isicCreditScraper: IsicCreditScraper) {

    fun getIsicCredit(): String {
        return isicCreditScraper.checkIsicCredit() ?: "0 Kč"
    }
}