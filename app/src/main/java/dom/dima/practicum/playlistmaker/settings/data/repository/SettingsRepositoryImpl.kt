package dom.dima.practicum.playlistmaker.settings.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.settings.domain.models.ThemeSettings

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository,
    ApplicationConstants {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(
            sharedPreferences.getBoolean(DARK_THEME_KEY, false)
        )
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit {
            putBoolean(DARK_THEME_KEY, settings.isDarkTheme)
        }

    }
}