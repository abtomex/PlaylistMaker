package dom.dima.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import dom.dima.practicum.playlistmaker.sharing.domain.ExternalNavigator
import dom.dima.practicum.playlistmaker.sharing.models.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(shareAppLink: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
        }
        context.startActivity(shareIntent)
    }

    override fun openLink(termsLink: String) {
        val offerIntent = Intent(Intent.ACTION_VIEW)
        offerIntent.data = termsLink.toUri()
        context.startActivity(offerIntent)
    }

    override fun openEmail(supportEmailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, supportEmailData.email)
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
        }
        context.startActivity(supportIntent)

    }
}