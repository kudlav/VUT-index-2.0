package prvnimilion.vutindex.webscraper.scrapers

import org.jsoup.Jsoup
import prvnimilion.vutindex.webscraper.util.INDEX_URL
import prvnimilion.vutindex.webscraper.util.LOGIN_URL
import prvnimilion.vutindex.webscraper.util.REQUEST_URL
import prvnimilion.vutindex.webscraper.util.VutCookieStore
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class LoginScraper(private val vutCookieStore: VutCookieStore) {

    fun login(username: String, password: String): Boolean {
        return try {
            Timber.tag("VUTdebug").d("Log in ...")
            clearCookies()

            val response = Jsoup.connect(LOGIN_URL)
                .userAgent(USER_AGENT)
                .execute()

            val cookies = response.cookies()
            cookies["fontsLoaded"] = "true"

            val credentials = mutableMapOf<String, String>()
            credentials[USERNAME_KEY] = URLEncoder.encode(username, "UTF-8")
            credentials[PASSWORD_KEY] = URLEncoder.encode(password, "UTF-8")

            val loginPage = response.parse()

            credentials[P4_FORM] = "1"
            credentials[LOGIN_FORM] = "1"
            credentials[SENT_TIME] =
                loginPage.getElementsByAttributeValue("name", SENT_TIME).`val`()
            credentials[FD_KEY] = loginPage.getElementsByAttributeValue("name", FD_KEY).`val`()

            val postData = StringBuilder()
            credentials.forEach {
                if (postData.isNotEmpty()) postData.append('&')
                postData.append("${it.key}=${it.value}")
            }

            var cookieString = ""
            cookies.forEach {
                cookieString += String.format("%s=%s; ", it.key, it.value)
            }
            cookieString = cookieString.substring(0, cookieString.length - 2)

            val url = URL(REQUEST_URL)
            val con = url.openConnection()
            val http = con as HttpURLConnection
            http.requestMethod = "POST"
            http.setRequestProperty("User-Agent", USER_AGENT)
            http.setRequestProperty("Cookie", cookieString)

            http.doInput = true
            http.doOutput = true
            http.instanceFollowRedirects = false
            http.connect()

            val writer = OutputStreamWriter(http.outputStream, "UTF-8")
            Timber.d(postData.toString())
            writer.write(postData.toString())
            writer.close()

            val inputStream = BufferedReader(InputStreamReader(con.inputStream))
            val buffer = StringBuffer()
            inputStream.forEachLine {
                buffer.append(it)
                Timber.d(it)
            }
            inputStream.close()

            val httpCookies = http.headerFields["Set-Cookie"]
            httpCookies?.forEach {
                val key = it.split("=")[0]
                val value = it.split("=")[1].split(";")[0]
                cookies[key] = value
            }

            isLoggedIn(cookies)
        } catch (e: Exception) {
            Timber.e(e.toString())
            false
        }
    }

    fun refreshSession(): Boolean {
        try {
            Timber.tag("VUTdebug").d("Refresh session ...")

            val cookies = vutCookieStore.loadCookies()

            val index = Jsoup.connect(INDEX_URL)
                .followRedirects(true)
                .cookies(cookies)
                .execute()

            cookies.putAll(index.cookies())
            return isLoggedIn(cookies)
        } catch (e: Exception) {
            Timber.e(e.toString())
            return false
        }
    }

    private fun isLoggedIn(cookies: MutableMap<String, String>): Boolean {
        if (cookies["portal_is_logged_in"].isNullOrBlank()) {
            return false
        } else if (cookies["portal_is_logged_in"]!! != "1") {
            return false
        }

        Timber.tag("VUTdebug").d("Successfully logged in ...")
        vutCookieStore.saveCookies(cookies)
        return true
    }

    fun getLoginFdKey(): String {
        return try {
            val response = Jsoup.connect(LOGIN_URL)
                .userAgent(USER_AGENT)
                .execute()
            val loginPage = response.parse()

            loginPage.getElementsByAttributeValue("name", FD_KEY).`val`()
        } catch (e: Exception) {
            Timber.e(e.toString())
            ""
        }
    }

    fun clearCookies() {
        vutCookieStore.clearCookies()
    }

    companion object {
        const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0"

        const val USERNAME_KEY = "LDAPlogin"
        const val PASSWORD_KEY = "LDAPpasswd"

        const val P4_FORM = "special_p4_form"
        const val LOGIN_FORM = "login_form"
        const val SENT_TIME = "sentTime"
        const val FD_KEY = "sv[fdkey]"
    }
}