package dom.dima.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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

    fun doShare() {
        sharingInteractor.shareApp()
    }

    fun changeTheme(checked: Boolean) {
        settingsInteractor.updateThemeSetting(
            dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings(
                checked
            )
        )
    }

    fun doWrightTechSupport() {
        sharingInteractor.openSupport()
    }

    fun showAgreement() {
        sharingInteractor.openTerms()
    }


    companion object {
        fun getFactory(sharingInteractor: SharingInteractor, settingsInteractor: SettingsInteractor): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(sharingInteractor, settingsInteractor)
            }
        }
    }
}