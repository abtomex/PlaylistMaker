package dom.dima.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import dom.dima.practicum.playlistmaker.data.TracksRepositoryImpl
import dom.dima.practicum.playlistmaker.data.network.RetrofitNetworkClient
import dom.dima.practicum.playlistmaker.data.settings.SettingsRepository
import dom.dima.practicum.playlistmaker.data.settings.SettingsRepositoryImpl
import dom.dima.practicum.playlistmaker.data.sharing.ExternalNavigator
import dom.dima.practicum.playlistmaker.domain.api.TracksInteractor
import dom.dima.practicum.playlistmaker.domain.api.TracksRepository
import dom.dima.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import dom.dima.practicum.playlistmaker.domain.settings.SettingsInteractor
import dom.dima.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import dom.dima.practicum.playlistmaker.domain.sharing.SharingInteractor
import dom.dima.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigator(context)
    }

    fun provideSharingInteractor(context: Context) : SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context), context)
    }

    private fun getSettingsRepository(sharedPreferences: SharedPreferences) : SettingsRepository {
        return SettingsRepositoryImpl(sharedPreferences)
    }

    fun provideSettingsInteractor(sharedPreferences: SharedPreferences): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(sharedPreferences))
    }
}