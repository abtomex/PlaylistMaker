package dom.dima.practicum.playlistmaker.di

import android.content.Context
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.media.data.FavoritesRepositoryImpl
import dom.dima.practicum.playlistmaker.media.data.converters.TrackDbConverter
import dom.dima.practicum.playlistmaker.media.domain.FavoritesRepository
import dom.dima.practicum.playlistmaker.search.data.TracksRepositoryImpl
import dom.dima.practicum.playlistmaker.search.domain.TracksRepository
import dom.dima.practicum.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import dom.dima.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    // SharedPreferences
    single {
        androidContext()
            .getSharedPreferences("Application_preferences", Context.MODE_PRIVATE)
    }

    // Gson
    factory { Gson() }

    factory { TrackDbConverter() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

}