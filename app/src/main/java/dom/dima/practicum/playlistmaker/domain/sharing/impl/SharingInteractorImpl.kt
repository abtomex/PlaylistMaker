package dom.dima.practicum.playlistmaker.domain.sharing.impl

import android.content.Context
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.data.sharing.ExternalNavigator
import dom.dima.practicum.playlistmaker.domain.sharing.SharingInteractor
import dom.dima.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
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
        return context.getString(R.string.android_course_url)
    }

    private fun getSupportEmailData(): EmailData {

        return EmailData(
            arrayOf(context.getString(R.string.my_email)),
            context.getString(R.string.email_subject),
            context.getString(R.string.email_text)
        )

    }

    private fun getTermsLink(): String {
        return context.getString(R.string.practicum_offer)
    }
}