package dom.dima.practicum.playlistmaker.media.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dom.dima.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist


class PlaylistDbConverter (
    val gson: Gson
) {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.coverImgName,
            System.currentTimeMillis(),
            gson.toJson(playlist.trackIds)
        )
    }

    fun map(playlistEntity: PlaylistEntity) : Playlist {

        val type = object : TypeToken<IntArray>() {}.type

        return Playlist (
            playlistEntity.id,
            playlistEntity.title,
            playlistEntity.description,
            playlistEntity.coverImgName,
            playlistEntity.createdTs,
            gson.fromJson(playlistEntity.trackIds, type)
        )
    }
}