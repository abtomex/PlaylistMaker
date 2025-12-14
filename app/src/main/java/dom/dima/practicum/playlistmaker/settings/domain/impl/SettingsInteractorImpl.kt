package dom.dima.practicum.playlistmaker.settings.domain.impl

import dom.dima.practicum.playlistmaker.settings.data.repository.SettingsRepository
import dom.dima.practicum.playlistmaker.settings.domain.SettingsInteractor
import dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings

class SettingsInteractorImpl (
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {
    
    override fun getThemeSettings(): dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}