package dom.dima.practicum.playlistmaker.service

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.data.Track

class SearchHistoryService(private val sharedPreferences: SharedPreferences) : ApplicationConstants {

    private val tracks = ArrayList<Track>()

    init {
        tracks.addAll(
            listOf(
                Track(
                    "Smells Like Teen Spirit",
                    "Nirvana",
                    501,
                    "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
                ),
                Track(
                    "Billie Jean",
                    "Michael Jackson",
                    435,
                    "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
                ),

                Track(
                    "Stayin' Alive",
                    "Bee Gees",
                    410,
                    "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
                ),

                Track(
                    "Whole Lotta Love",
                    "Led Zeppelin",
                    533,
                    "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
                ),

                Track(
                    "Sweet Child O'Mine",
                    "Guns N' Roses",
                    503,
                    "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg "
                ),
                Track(
                    "Smells Like Teen Spirit Smells Like Teen Spirit Smells Like Teen Spirit",
                    "Nirvana Smells Like Teen Spirit Smells Like Teen Spirit Nirvana Smells Like Teen Spirit Smells Like Teen Spirit",
                    501,
                    ""
                )

            )
        )
    }

    fun getHistory() : MutableList<Track> {

        val json = Gson().toJson(tracks)
        sharedPreferences.edit {
            putString(TRACK_HISTORY, json)
        }

        val json1 = sharedPreferences.getString(TRACK_HISTORY, null)
        val type = object : TypeToken<List<Track>>() {}.type

        return Gson().fromJson(json1, type)

    }

    fun addToHistory(track: Track) {
        tracks.add(0, track)
    }

}