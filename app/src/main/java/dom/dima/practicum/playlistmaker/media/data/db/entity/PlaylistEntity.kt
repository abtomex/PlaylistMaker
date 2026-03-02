package dom.dima.practicum.playlistmaker.media.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
class PlaylistEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String?,
    val coverImgName: String?,
    @ColumnInfo("created_ts")
    val createdTs: Long,
    val trackIds: String?

)