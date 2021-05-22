package cz.kudlav.vutindex.webscraper.util

import android.content.Context
import com.loopj.android.http.PersistentCookieStore
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie

class VutCookieStore(appContext: Context) : PersistentCookieStore(appContext) {

    fun loadCookies(): MutableMap<String, String> {
        val savedCookies = mutableMapOf<String, String>()
        this.cookies.forEach {
            savedCookies[it.name] = it.value
        }

        return savedCookies
    }

    fun saveCookies(cookies: MutableMap<String, String>) {
        clearCookies()
        cookies.forEach { cookie ->
            val newCookie = BasicClientCookie(cookie.key, cookie.value)
            this.addCookie(newCookie)
        }
    }

    fun clearCookies() {
        clear()
    }
}