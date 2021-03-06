package cz.kudlav.vutindex.webscraper.scrapers

import org.jsoup.Connection
import org.jsoup.Jsoup
import cz.kudlav.vutindex.webscraper.util.KAM_URL
import cz.kudlav.vutindex.webscraper.util.VutCookieStore
import timber.log.Timber
import java.lang.Exception

class IsicCreditScraper(private val vutCookieStore: VutCookieStore) {

    fun checkIsicCredit(): String? {
        try {
            val cookies = vutCookieStore.loadCookies()
            val creditResponse: Connection.Response?
            try {
                creditResponse = Jsoup.connect(KAM_URL)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .cookies(cookies)
                    .execute()
            } catch (e: Exception) {
                return null
            }
            cookies.putAll(creditResponse!!.cookies())
            vutCookieStore.saveCookies(cookies)

            val creditDoc = creditResponse.parse()
            val credit = creditDoc.getElementsByClass("tdRight")
            return credit.text()
        } catch (e: Exception) {
            Timber.e(e.toString())
            return null
        }
    }

}