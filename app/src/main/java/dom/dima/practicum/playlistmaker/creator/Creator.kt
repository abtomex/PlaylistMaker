package dom.dima.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import dom.dima.practicum.playlistmaker.search.data.TracksRepositoryImpl
import dom.dima.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import dom.dima.practicum.playlistmaker.settings.data.repository.SettingsRepository
import dom.dima.practicum.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import dom.dima.practicum.playlistmaker.sharing.data.ExternalNavigator
import dom.dima.practicum.playlistmaker.search.domain.TracksInteractor
import dom.dima.practicum.playlistmaker.search.domain.TracksRepository
import dom.dima.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import dom.dima.practicum.playlistmaker.settings.domain.SettingsInteractor
import dom.dima.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import dom.dima.practicum.playlistmaker.sharing.domain.SharingInteractor
import dom.dima.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

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