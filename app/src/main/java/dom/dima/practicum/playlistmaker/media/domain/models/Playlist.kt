package dom.dima.practicum.playlistmaker.media.domain.models

import android.net.Uri
import dom.dima.practicum.playlistmaker.search.domain.models.Track

data class Playlist (
    val id: Int = 0,
    val title: String,
    val description: String?,
    val cover: Uri?,
    val trackIds: MutableSet<Int> = mutableSetOf(),
    val tracks: MutableList<Track> = mutableListOf()
)