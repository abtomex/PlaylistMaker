package dom.dima.practicum.playlistmaker.di

import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import dom.dima.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}