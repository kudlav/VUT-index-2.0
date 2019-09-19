package prvnimilion.vutindex.repository.repos

import prvnimilion.vutindex.repository.security.PreferenceProvider
import prvnimilion.vutindex.webscraper.scrapers.LoginScraper


class AuthRepository(private val loginScraper: LoginScraper, private val preferenceProvider: PreferenceProvider) {

    fun loginUser(username: String, password: String, saveCredentials: Boolean?): Boolean {
        if (saveCredentials == true) saveCredentials(username, password)
        else { saveCredentials(null, null)}
        return loginScraper.login(username, password)
    }

    fun refreshSession(): Boolean {
        return loginScraper.refreshSession()
    }

    private fun saveCredentials(username: String?, password: String?) {
        preferenceProvider.setUsername(username)
        preferenceProvider.setPassword(password)
    }

    fun getCredentials(): MutableMap<String, String> {
        val username = preferenceProvider.getUserName() ?: ""
        val password = preferenceProvider.getPassword() ?: ""

        return mutableMapOf(username to password)
    }
}

