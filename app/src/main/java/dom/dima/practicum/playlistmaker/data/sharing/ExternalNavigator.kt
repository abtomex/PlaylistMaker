package dom.dima.practicum.playlistmaker.data.sharing

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import dom.dima.practicum.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigator(private val context: Context) {

    fun shareLink(shareAppLink: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
        }
        context.startActivity(shareIntent)
    }

    fun openLink(termsLink: String) {
        val offerIntent = Intent(Intent.ACTION_VIEW)
        offerIntent.data = termsLink.toUri()
        context.startActivity(offerIntent)
    }

    fun openEmail(supportEmailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, supportEmailData.email)
            putExtra(Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.text)
        }
        context.startActivity(supportIntent)

    }
}