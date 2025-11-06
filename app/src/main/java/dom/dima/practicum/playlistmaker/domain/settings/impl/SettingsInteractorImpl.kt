package dom.dima.practicum.playlistmaker.domain.settings.impl

import dom.dima.practicum.playlistmaker.data.settings.SettingsRepository
import dom.dima.practicum.playlistmaker.domain.settings.SettingsInteractor
import dom.dima.practicum.playlistmaker.domain.settings.model.ThemeSettings

class SettingsInteractorImpl (
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {
    
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}