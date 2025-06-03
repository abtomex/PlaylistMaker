package dom.dima.practicum.playlistmaker.api

import dom.dima.practicum.playlistmaker.data.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchTrackApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse>
}