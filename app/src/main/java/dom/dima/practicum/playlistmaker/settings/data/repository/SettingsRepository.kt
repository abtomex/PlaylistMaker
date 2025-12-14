package dom.dima.practicum.playlistmaker.settings.data.repository

import dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}