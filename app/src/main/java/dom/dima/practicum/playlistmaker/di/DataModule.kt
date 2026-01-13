package dom.dima.practicum.playlistmaker.di

import android.content.Context
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.search.data.network.NetworkClient
import dom.dima.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import dom.dima.practicum.playlistmaker.search.data.network.SearchTrackApiService
import org.koin.android.ext.koin.androidContext
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
    // SharedPreferences
    single {
        androidContext()
            .getSharedPreferences("Application_preferences", Context.MODE_PRIVATE)
    }

    // Gson
    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

}