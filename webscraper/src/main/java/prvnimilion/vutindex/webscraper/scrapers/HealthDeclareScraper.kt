package prvnimilion.vutindex.webscraper.scrapers

import org.jsoup.Connection
import org.jsoup.Jsoup
import prvnimilion.vutindex.webscraper.util.HEALTH_GET_URL
import prvnimilion.vutindex.webscraper.util.HEALTH_SIGN_URL
import prvnimilion.vutindex.webscraper.util.VutCookieStore
import timber.log.Timber
import java.lang.Exception

class HealthDeclareScraper(private val vutCookieStore: VutCookieStore) {

    fun getDeclarationState(): String? {
        try {
            val cookies = vutCookieStore.loadCookies()
            val healthResponse: Connection.Response?
            try {
                healthResponse = Jsoup.connect(HEALTH_GET_URL)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .timeout(30 * 1000)
                    .cookies(cookies)
                    .execute()
            } catch (e: Exception) {
                return null
            }
            cookies.putAll(healthResponse!!.cookies())
            vutCookieStore.saveCookies(cookies)

            val healthDoc = healthResponse.parse()
            val health = healthDoc.getElementsByClass("alert-text")
            return health.text()
        } catch (e: Exception) {
            Timber.e(e.toString())
            return null
        }
    }

    fun signDeclaration(): String? {
        try {
            val cookies = vutCookieStore.loadCookies()
            val healthResponse: Connection.Response?
            try {
                healthResponse = Jsoup.connect(HEALTH_SIGN_URL)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .timeout(30 * 1000)
                    .cookies(cookies)
                    .execute()
            } catch (e: Exception) {
                return null
            }
            cookies.putAll(healthResponse!!.cookies())
            vutCookieStore.saveCookies(cookies)

            val healthDoc = healthResponse.parse()
            val health = healthDoc.getElementsByClass("alert-text").first()
            return health.text()
        } catch (e: Exception) {
            Timber.e(e.toString())
            return null
        }
    }

}