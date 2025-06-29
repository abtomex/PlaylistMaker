package dom.dima.practicum.playlistmaker.data

data class Track (val trackId: Int, val trackName: String, val artistName: String, val trackTimeMillis: Long, val artworkUrl100: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Track

        return trackId == other.trackId
    }

    override fun hashCode(): Int {
        return trackId
    }
}

