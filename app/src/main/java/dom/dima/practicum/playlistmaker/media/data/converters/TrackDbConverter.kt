package dom.dima.practicum.playlistmaker.media.data.converters

import dom.dima.practicum.playlistmaker.media.data.db.entity.TrackEntity
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale


class TrackDbConverter {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            System.currentTimeMillis(),
            track.isFavorite
        )
    }

    @JvmName("mapNullable")
    fun map(trackEntity: TrackEntity?) : Track? {
        return trackEntity?.let { map(it) }
    }
    @JvmName("mapNotNull")
    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            trackTimeStr = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(trackEntity.trackTimeMillis),
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            previewUrl = trackEntity.previewUrl,
            isFavorite = trackEntity.isFavorite
        )
    }
}