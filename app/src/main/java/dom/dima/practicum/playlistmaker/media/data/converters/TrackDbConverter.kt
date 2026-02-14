package dom.dima.practicum.playlistmaker.media.data.converters

import dom.dima.practicum.playlistmaker.media.data.db.entity.FavoriteEntity
import dom.dima.practicum.playlistmaker.search.domain.models.Track


class TrackDbConverter {
    fun map(track: Track): FavoriteEntity {
        return FavoriteEntity(
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
            System.currentTimeMillis()
        )
    }

    fun map(favoriteEntity: FavoriteEntity): Track {
        return Track(
            favoriteEntity.trackId,
            favoriteEntity.trackName,
            favoriteEntity.artistName,
            favoriteEntity.trackTimeMillis,
            favoriteEntity.artworkUrl100,
            favoriteEntity.collectionName,
            favoriteEntity.releaseDate,
            favoriteEntity.primaryGenreName,
            favoriteEntity.country,
            favoriteEntity.previewUrl
        )
    }
}