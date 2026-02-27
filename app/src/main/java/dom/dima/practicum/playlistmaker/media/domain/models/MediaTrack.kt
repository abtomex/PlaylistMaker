package dom.dima.practicum.playlistmaker.media.domain.models

data class MediaTrack (

    val id: Int,
    val trackId: Int,
    val trackName: String,
    val artistName: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,

)