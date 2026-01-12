package dom.dima.practicum.playlistmaker.sharing.domain.impl

import dom.dima.practicum.playlistmaker.sharing.domain.ExternalNavigator
import dom.dima.practicum.playlistmaker.sharing.domain.SharingInteractor
import dom.dima.practicum.playlistmaker.sharing.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(shareAppLink: String) {
        externalNavigator.shareLink(shareAppLink)
    }

    override fun openTerms(termsLink: String) {
        externalNavigator.openLink(termsLink)
    }

    override fun openSupport(email: Array<String>, emailSubject: String, emailText: String) {
        externalNavigator.openEmail(getSupportEmailData(email, emailSubject, emailText))
    }


    private fun getSupportEmailData(email: Array<String>, emailSubject: String, emailText: String): EmailData {

        return EmailData(
            email,
            emailSubject,
            emailText
        )

    }

}