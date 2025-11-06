package dom.dima.practicum.playlistmaker.domain.settings

import dom.dima.practicum.playlistmaker.domain.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}