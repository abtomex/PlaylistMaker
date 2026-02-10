package dom.dima.practicum.playlistmaker.media.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_track_table")
class FavoriteEntity (
    @PrimaryKey
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
//    Комментарий для ревьюера:
//    Поле, которое я использую для сортировки избранного по порядку добавления
    @ColumnInfo("created_ts")
    val createdTs: Long

)
