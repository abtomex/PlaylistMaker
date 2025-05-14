package dom.dima.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.net.toUri

class SettingsActivity : AbstractButtonBackActivity() {

    override fun buttonBackId(): Int {
        return R.id.settings_layout
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switch = findViewById<SwitchCompat>(R.id.night_theme_switch)
        val prefs = getSharedPreferences(SETTINGS, MODE_PRIVATE)
        val isDark = prefs.getBoolean(DARK_MODE, false)

        switch.isChecked = isDark
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

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


    companion object {
        const val SETTINGS = "settings"
        const val DARK_MODE = "dark_mode"
    }

}
