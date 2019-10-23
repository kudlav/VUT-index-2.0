package prvnimilion.vutindex.repository.repos

import prvnimilion.vutindex.repository.security.PreferenceProvider
import prvnimilion.vutindex.webscraper.scrapers.MessagesScraper

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
        val newMsgTitle = messagesScraper.checkNewMessages()
        if (!newMsgTitle.isNullOrEmpty()) {
            val lastMsgTitle = getLastMessage()
            if (newMsgTitle != lastMsgTitle) {
                setLastMessage(newMsgTitle)
                return lastMsgTitle != ""
            }
            return false
        }
        return false
    }
}