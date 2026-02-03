package dom.dima.practicum.playlistmaker.root.view_model

import androidx.lifecycle.ViewModel
import dom.dima.practicum.playlistmaker.settings.domain.SettingsInteractor

class RootViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun actualizeTheme() {
        settingsInteractor.updateThemeSetting(settingsInteractor.getThemeSettings())
    }
}