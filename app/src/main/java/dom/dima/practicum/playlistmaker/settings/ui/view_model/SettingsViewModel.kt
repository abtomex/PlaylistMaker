package dom.dima.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import dom.dima.practicum.playlistmaker.settings.domain.SettingsInteractor
import dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings
import dom.dima.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    fun isDarkThemeOn(): Boolean {
        return settingsInteractor.getThemeSettings().isDarkTheme
    }

    fun doShare(courseUrl: String) {
        sharingInteractor.shareApp(courseUrl)
    }

    fun changeTheme(checked: Boolean) {
        settingsInteractor.updateThemeSetting(
            ThemeSettings(
                checked
            )
        )
    }

    fun doWrightTechSupport(email: Array<String>, emailSubject: String, emailText: String) {
        sharingInteractor.openSupport(
            email,
            emailSubject,
            emailText
        )
    }

    fun showAgreement(termsLink: String) {
        sharingInteractor.openTerms(termsLink)
    }

}