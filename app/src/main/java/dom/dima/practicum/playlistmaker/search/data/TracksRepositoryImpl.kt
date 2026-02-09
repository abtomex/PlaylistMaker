package dom.dima.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import dom.dima.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import dom.dima.practicum.playlistmaker.search.data.network.NetworkClient
import dom.dima.practicum.playlistmaker.search.domain.TracksRepository
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.settings.domain.api.ApiResponse
import java.util.Objects

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : TracksRepository, ApplicationConstants {

    val tracks = ArrayList<Track>()

    init {
        val json = sharedPreferences.getString(TRACK_HISTORY, ArrayList<Track>().toString())
        val type = object : TypeToken<List<Track>>() {}.type
        tracks.addAll(gson.fromJson(json, type))
    }


    override suspend fun searchTracks(searchStr: String): ApiResponse<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(searchStr))
        return when (response.resultCode) {
            -1 -> ApiResponse.NoInternet("Проверьте подключение к интернету")
            200 -> {
                ApiResponse.Success((response as TracksSearchResponse).results.filter {
                    Objects.nonNull(
                        it
                    )
                }.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                })
            }

            else -> ApiResponse.Error("response result code is ${response.resultCode}")
        }
        /*
                if (response.resultCode == 200) {
                    return ApiResponse.Success((response as TracksSearchResponse).results.filter {
                        Objects.nonNull(
                            it
                        )
                    }.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    })
                } else {
                    return ApiResponse.Error("response result code is ${response.resultCode}")
                }
        */
    }

    override suspend fun historyTracks(): ApiResponse<List<Track>> {
        return ApiResponse.Success(tracks)
    }

    override fun addToHistory(track: Track) {

        if (tracks.contains(track)) {
            tracks.remove(track)
        }
        tracks.add(0, track)

        while (tracks.size > MAX_HISTORY_SIZE) {
            tracks.removeAt(MAX_HISTORY_SIZE)
        }
        reloadSharedPreferences()
    }

    override fun clearHistory() {
        tracks.clear()
        reloadSharedPreferences()

    }

    private fun reloadSharedPreferences() {
        val json = gson.toJson(tracks)
        sharedPreferences.edit {
            putString(TRACK_HISTORY, json)
        }
    }


}