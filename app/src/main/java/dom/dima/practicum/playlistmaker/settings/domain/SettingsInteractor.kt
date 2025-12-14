package dom.dima.practicum.playlistmaker.settings.domain

import dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings
    fun updateThemeSetting(settings: dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings)
}