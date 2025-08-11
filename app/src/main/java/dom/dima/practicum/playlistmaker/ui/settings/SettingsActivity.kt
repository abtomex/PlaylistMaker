package dom.dima.practicum.playlistmaker.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.core.net.toUri
import com.google.android.material.switchmaterial.SwitchMaterial
import dom.dima.practicum.playlistmaker.App
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.ui.AbstractButtonBackActivity

class SettingsActivity : AbstractButtonBackActivity(), ApplicationConstants {

    override fun buttonBackId(): Int {
        return R.id.settings_layout
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val sharedPrefs = getSharedPreferences(APPLICATION_PREFERENCES, MODE_PRIVATE)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_THEME_KEY, false)

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        val buttonShare = findViewById<TextView>(R.id.share_text)
        buttonShare.setOnClickListener {

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_url))
            }

            startActivity(shareIntent)

        }

        val buttonCheckSupport = findViewById<TextView>(R.id.helpdesk_text)
        buttonCheckSupport.setOnClickListener {

            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
        }
            startActivity(supportIntent)
        }

        val buttonAgreement = findViewById<TextView>(R.id.agreement_text)
        buttonAgreement.setOnClickListener {
            val practicumOffer = getString(R.string.practicum_offer)
            val offerIntent = Intent(Intent.ACTION_VIEW)
            offerIntent.data = practicumOffer.toUri()

            startActivity(offerIntent)
        }
    }

}
