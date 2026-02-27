package dom.dima.practicum.playlistmaker.media.data.converters

import dom.dima.practicum.playlistmaker.media.data.db.entity.TrackEntity
import dom.dima.practicum.playlistmaker.search.domain.models.Track


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
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl,
            trackEntity.isFavorite
        )
    }
}