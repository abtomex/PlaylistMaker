package dom.dima.practicum.playlistmaker.settings.domain

import dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}