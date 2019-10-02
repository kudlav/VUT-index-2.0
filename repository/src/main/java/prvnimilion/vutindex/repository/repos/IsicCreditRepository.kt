package prvnimilion.vutindex.repository.repos

import prvnimilion.vutindex.webscraper.scrapers.IsicCreditScraper
import timber.log.Timber

class IsicCreditRepository(private val isicCreditScraper: IsicCreditScraper) {

    fun getIsicCredit(): String {
        var credit: String? = null
        try {
            credit = isicCreditScraper.checkIsicCredit()
        } catch (e: Exception) {
            Timber.e(e.toString())
        }
        return credit ?: "0 Kč"
    }
}