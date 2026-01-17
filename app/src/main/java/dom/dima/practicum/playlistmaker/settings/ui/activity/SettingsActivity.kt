package dom.dima.practicum.playlistmaker.settings.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial
import dom.dima.practicum.playlistmaker.AbstractButtonBackActivity
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AbstractButtonBackActivity(), ApplicationConstants {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun buttonBackId(): Int {
        return R.id.settings_layout
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val buttonShare = findViewById<TextView>(R.id.share_text)
        val buttonCheckSupport = findViewById<TextView>(R.id.helpdesk_text)
        val buttonAgreement = findViewById<TextView>(R.id.agreement_text)

        themeSwitcher.isChecked = viewModel.isDarkThemeOn()
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeTheme(checked)
        }
        buttonShare.setOnClickListener {
            viewModel.doShare(getString(R.string.android_course_url))
        }

        buttonCheckSupport.setOnClickListener {
            viewModel.doWrightTechSupport(
                arrayOf(getString(R.string.my_email)),
                getString(R.string.email_subject),
                getString(R.string.email_text)
            )
        }

        buttonAgreement.setOnClickListener {
            viewModel.showAgreement(getString(R.string.practicum_offer))
        }

    }
}
