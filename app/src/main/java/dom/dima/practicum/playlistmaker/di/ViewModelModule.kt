package dom.dima.practicum.playlistmaker.di

import android.media.MediaPlayer
import dom.dima.practicum.playlistmaker.media.view_model.FavoriteTracksViewModel
import dom.dima.practicum.playlistmaker.media.view_model.MediaViewModel
import dom.dima.practicum.playlistmaker.media.view_model.PlaylistsViewModel
import dom.dima.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import dom.dima.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get())
    }

    viewModel {
        MediaViewModel()
    }

    viewModel {
        FavoriteTracksViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }

    factory <MediaPlayer> {
        MediaPlayer()
    }
}