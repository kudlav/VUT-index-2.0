package cz.kudlav.vutindex.repository.repos

import cz.kudlav.vutindex.repository.security.PreferenceProvider
import cz.kudlav.vutindex.webscraper.scrapers.MessagesScraper
import timber.log.Timber

class MessagesRepository(
    private val messagesScraper: MessagesScraper,
    private val preferenceProvider: PreferenceProvider
) {

    private fun setLastMessage(msgTitle: String) {
        preferenceProvider.setLastMessage(msgTitle)
    }

    private fun getLastMessage(): String {
        return preferenceProvider.getLastMessage() ?: ""
    }

    fun isNewMessage(): Boolean {
        Timber.tag("VutIndexWorker").d("Searching for new messages")
        val newMsgTitle = messagesScraper.checkNewMessages()
        if (!newMsgTitle.isNullOrEmpty()) {
            val lastMsgTitle = getLastMessage()
            if (newMsgTitle != lastMsgTitle) {
                Timber.tag("VutIndexWorker").d("New message!")

                setLastMessage(newMsgTitle)
                return lastMsgTitle != ""
            }
            return false
        }
        return false
    }
}