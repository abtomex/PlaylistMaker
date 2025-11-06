package dom.dima.practicum.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import dom.dima.practicum.playlistmaker.domain.settings.SettingsInteractor
import dom.dima.practicum.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    // Основной код
}