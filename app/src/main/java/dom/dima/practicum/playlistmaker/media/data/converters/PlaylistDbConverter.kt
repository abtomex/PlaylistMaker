package dom.dima.practicum.playlistmaker.media.data.converters

import android.content.Context
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dom.dima.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import java.io.File


class PlaylistDbConverter (
    val gson: Gson,
    val context: Context
) {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.cover?.toFile()?.name,
            System.currentTimeMillis(),
            gson.toJson(playlist.trackIds)
        )
    }

    fun map(playlistEntity: PlaylistEntity) : Playlist {

        val type = object : TypeToken<Set<Int>>() {}.type
        val filePath = File( context.filesDir, "pm")
        val file = if (playlistEntity.coverImgName != null)
            File(filePath, playlistEntity.coverImgName)
        else null

        return Playlist (
            playlistEntity.id,
            playlistEntity.title,
            playlistEntity.description,
            file?.toUri(),
            gson.fromJson(playlistEntity.trackIds, type)
        )
    }
}