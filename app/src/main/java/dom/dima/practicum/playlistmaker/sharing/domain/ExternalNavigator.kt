package dom.dima.practicum.playlistmaker.sharing.domain

import dom.dima.practicum.playlistmaker.sharing.models.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(termsLink: String)
    fun openEmail(supportEmailData: EmailData)
}