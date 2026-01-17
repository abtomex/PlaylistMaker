package dom.dima.practicum.playlistmaker.di

import dom.dima.practicum.playlistmaker.search.data.TracksRepositoryImpl
import dom.dima.practicum.playlistmaker.search.domain.TracksRepository
import dom.dima.practicum.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import dom.dima.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

}