package dom.dima.practicum.playlistmaker.settings.data.repository

import dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings
    fun updateThemeSetting(settings: dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings)
}