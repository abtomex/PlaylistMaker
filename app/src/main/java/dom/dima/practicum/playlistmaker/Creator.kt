package dom.dima.practicum.playlistmaker

import dom.dima.practicum.playlistmaker.data.TracksRepositoryImpl
import dom.dima.practicum.playlistmaker.data.network.RetrofitNetworkClient
import dom.dima.practicum.playlistmaker.domain.api.TracksInteractor
import dom.dima.practicum.playlistmaker.domain.api.TracksRepository
import dom.dima.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}