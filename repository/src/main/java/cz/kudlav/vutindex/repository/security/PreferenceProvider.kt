package cz.kudlav.vutindex.repository.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

private const val SECRET_SHARED_PREFERENCES = "secret_shared_preferences"
private const val USERNAME_KEY = "42"
private const val PASSWORD_KEY = "101"
private const val MESSAGES_KEY = "666"

class PreferenceProvider(context: Context) {
    private var masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        SECRET_SHARED_PREFERENCES,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setUsername(username: String?) {
        if (!username.isNullOrEmpty()) {
            encryptedSharedPreferences.edit().putString(USERNAME_KEY, username).apply()
        } else {
            encryptedSharedPreferences.edit().remove(USERNAME_KEY).apply()
        }
    }

    fun getUserName(): String? {
        return encryptedSharedPreferences.getString(USERNAME_KEY, null)
    }

    fun setPassword(password: String?) {
        if (!password.isNullOrEmpty()) {
            encryptedSharedPreferences.edit().putString(PASSWORD_KEY, password).apply()
        } else {
            encryptedSharedPreferences.edit().remove(PASSWORD_KEY).apply()
        }
    }

    fun getPassword(): String? {
        return encryptedSharedPreferences.getString(PASSWORD_KEY, null)
    }

    fun removeCredentials() {
        encryptedSharedPreferences.edit().putString(USERNAME_KEY, null).apply()
        encryptedSharedPreferences.edit().putString(PASSWORD_KEY, null).apply()
    }

    fun setLastMessage(msgTitle: String) {
        encryptedSharedPreferences.edit().putString(MESSAGES_KEY, msgTitle).apply()
    }

    fun getLastMessage(): String? {
        return encryptedSharedPreferences.getString(MESSAGES_KEY, "")
    }
}




