package dom.dima.practicum.playlistmaker.di

import dom.dima.practicum.playlistmaker.search.domain.TracksInteractor
import dom.dima.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import dom.dima.practicum.playlistmaker.settings.domain.SettingsInteractor
import dom.dima.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import dom.dima.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import dom.dima.practicum.playlistmaker.sharing.domain.ExternalNavigator
import dom.dima.practicum.playlistmaker.sharing.domain.SharingInteractor
import dom.dima.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val interactorModule = module {

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    single<TracksInteractor> {
        TracksInteractorImpl(get(), get())
    }
    //Executor
    single<Executor> {
        Executors.newCachedThreadPool()
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

}