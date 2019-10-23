package prvnimilion.vutindex.webscraper.scrapers

import org.jsoup.Connection
import org.jsoup.Jsoup
import prvnimilion.vutindex.webscraper.util.INTRA_URL
import prvnimilion.vutindex.webscraper.util.VutCookieStore
import timber.log.Timber
import java.lang.Exception

class MessagesScraper(private val vutCookieStore: VutCookieStore) {

    fun checkNewMessages(): String? {
        return try {
            val cookies = vutCookieStore.loadCookies()
            val messagesResponse: Connection.Response?
            try {
                messagesResponse = Jsoup.connect(INTRA_URL)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .timeout(10 * 1000)
                    .cookies(cookies)
                    .execute()
            } catch (e: Exception) {
                return ""
            }
            cookies.putAll(messagesResponse!!.cookies())
            vutCookieStore.saveCookies(cookies)

            val messagesDoc = messagesResponse.parse()
            val firstMsg =
                messagesDoc.getElementsByClass("table table-striped table-bordered").first()
            firstMsg.child(1).child(0).child(0).child(0).text()
        } catch (e: Exception) {
            Timber.e(e.toString())
            null
        }
    }

}