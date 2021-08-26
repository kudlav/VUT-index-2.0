package cz.kudlav.vutindex.webscraper.scrapers

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import cz.kudlav.vutindex.webscraper.util.HEALTH_URL
import cz.kudlav.vutindex.webscraper.util.VutCookieStore
import timber.log.Timber
import java.lang.Exception

class HealthDeclareScraper(private val vutCookieStore: VutCookieStore) {

    private var nonce: String = ""

    fun getDeclarationState(): String? {
        try {
            val cookies = vutCookieStore.loadCookies()
            val healthResponse: Connection.Response?
            try {
                healthResponse = Jsoup.connect(HEALTH_URL)
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
            parseNonce(healthDoc)
            val health = healthDoc.getElementsByClass("alert-text")
            return health.text()
        } catch (e: Exception) {
            Timber.e(e.toString())
            return null
        }
    }

    fun signDeclaration(): String? {
        if (nonce.isEmpty()) {
            // Try to get nonce if there is no nonce from previous query
            getDeclarationState()
            if (nonce.isEmpty()) return null
        }
        try {
            val cookies = vutCookieStore.loadCookies()
            val healthResponse: Connection.Response?
            try {
                healthResponse = Jsoup.connect(HEALTH_URL)
                    .method(Connection.Method.POST)
                    .data("formID", "prohlaseni-o-bezinfekcnosti-2")
                    .data("xs_prohlaseni__o__bezinfekcnosti__2", nonce)
                    .data("btnPodepsat-2", "1")
                    .followRedirects(true)
                    .timeout(30 * 1000)
                    .cookies(cookies)
                    .execute()
            } catch (e: Exception) {
                return null
            }
            cookies.putAll(healthResponse!!.cookies())
            vutCookieStore.saveCookies(cookies)

            val healthDoc = healthResponse.parse()
            parseNonce(healthDoc)
            val health = healthDoc.getElementsByClass("alert-text").first()
            return health?.text()
        } catch (e: Exception) {
            Timber.e(e.toString())
            return null
        }
    }

    private fun parseNonce(doc: Document) {
        val element = doc.getElementById("xs_prohlaseni__o__bezinfekcnosti__2")
        if (element != null) nonce = element.attr("value")
    }

}
