package cz.kudlav.vutindex.repository.repos

import cz.kudlav.vutindex.repository.security.PreferenceProvider
import cz.kudlav.vutindex.webscraper.scrapers.LoginScraper

class AuthRepository(
    private val loginScraper: LoginScraper,
    private val preferenceProvider: PreferenceProvider
) {

    fun loginUser(username: String, password: String): Boolean {
        saveCredentials(username, password)

        return loginScraper.login(username, password)
    }

    private fun refreshSession(): Boolean {
        return loginScraper.refreshSession()
    }

    private fun saveCredentials(username: String?, password: String?) {
        preferenceProvider.setUsername(username)
        preferenceProvider.setPassword(password)
    }

    fun getCredentials(): Pair<String, String> {
        val username = preferenceProvider.getUserName() ?: ""
        val password = preferenceProvider.getPassword() ?: ""

        return Pair(username, password)
    }

    fun quickLogin(): Boolean {
        val username = preferenceProvider.getUserName() ?: ""
        val password = preferenceProvider.getPassword() ?: ""
        if(username == "") return false
        return if (refreshSession()) true else loginScraper.login(username, password)
    }

    fun logoutUser(): Boolean {
        preferenceProvider.removeCredentials()
        loginScraper.clearCookies()
        return true
    }

    fun getLoginFdKey(): String {
        return loginScraper.getLoginFdKey()
    }
}

