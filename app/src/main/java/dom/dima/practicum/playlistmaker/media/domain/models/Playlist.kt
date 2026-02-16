package dom.dima.practicum.playlistmaker.media.domain.models

class Playlist (
    val id: Int,
    val title: String?,
    val description: String?,
    val coverImgName: String?,
    var createdTs: Long?,
    val trackIds: Array<Int>?
) {

    init {
        if (createdTs == null) {
            createdTs = System.currentTimeMillis()
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist

        if (id != other.id) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        return result
    }
}