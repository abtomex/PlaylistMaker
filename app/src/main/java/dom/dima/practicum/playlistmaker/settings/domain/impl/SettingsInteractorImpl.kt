package dom.dima.practicum.playlistmaker.settings.domain.impl

import dom.dima.practicum.playlistmaker.settings.domain.SettingsRepository
import dom.dima.practicum.playlistmaker.settings.domain.SettingsInteractor
import dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings

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