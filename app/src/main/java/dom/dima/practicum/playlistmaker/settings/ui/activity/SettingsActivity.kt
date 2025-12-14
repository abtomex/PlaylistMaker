package dom.dima.practicum.playlistmaker.settings.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.creator.Creator
import dom.dima.practicum.playlistmaker.AbstractButtonBackActivity
import dom.dima.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AbstractButtonBackActivity(), ApplicationConstants {

    private lateinit var viewModel: SettingsViewModel

    override fun buttonBackId(): Int {
        return R.id.settings_layout
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getFactory(
                Creator.provideSharingInteractor(this),
                Creator.provideSettingsInteractor(
                    getSharedPreferences(
                        APPLICATION_PREFERENCES,
                        MODE_PRIVATE
                    )
                )

            )
        ).get(SettingsViewModel::class.java)

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
            viewModel.doShare()
        }

        buttonCheckSupport.setOnClickListener {
            viewModel.doWrightTechSupport()
        }

        buttonAgreement.setOnClickListener {
            viewModel.showAgreement()
        }

    }
}
