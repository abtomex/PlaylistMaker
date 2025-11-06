package dom.dima.practicum.playlistmaker.domain.sharing.impl

import dom.dima.practicum.playlistmaker.domain.sharing.SharingInteractor
import dom.dima.practicum.playlistmaker.data.sharing.ExternalNavigator
import dom.dima.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,

    ) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        // Нужно реализовать
    }

    private fun getSupportEmailData(): EmailData {
        // Нужно реализовать
    }

    private fun getTermsLink(): String {
        // Нужно реализовать
    }
}