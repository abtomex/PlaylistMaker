package dom.dima.practicum.playlistmaker

import android.app.Application
import dom.dima.practicum.playlistmaker.di.dataModule
import dom.dima.practicum.playlistmaker.di.interactorModule
import dom.dima.practicum.playlistmaker.di.repositoryModule
import dom.dima.practicum.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlaylistMakerApplication : Application(), ApplicationConstants {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PlaylistMakerApplication)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

    }

}
