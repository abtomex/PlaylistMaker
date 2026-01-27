package dom.dima.practicum.playlistmaker.di

import dom.dima.practicum.playlistmaker.search.data.network.NetworkClient
import dom.dima.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import dom.dima.practicum.playlistmaker.search.data.network.SearchTrackApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    // Retrofit
    single<SearchTrackApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchTrackApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

}