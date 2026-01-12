package dom.dima.practicum.playlistmaker

import android.app.Application
import dom.dima.practicum.playlistmaker.creator.Creator
import dom.dima.practicum.playlistmaker.settings.domain.SettingsInteractor

class App : Application(), ApplicationConstants {

    lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        settingsInteractor = Creator.provideSettingsInteractor(getSharedPreferences(APPLICATION_PREFERENCES, MODE_PRIVATE))
        settingsInteractor.updateThemeSetting(settingsInteractor.getThemeSettings())

    }

}
