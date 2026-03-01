package dom.dima.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dom.dima.practicum.playlistmaker.media.data.db.entity.PlaylistEntity

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOne(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table order by created_ts desc")
    suspend fun getAll(): List<PlaylistEntity>

    @Query("DELETE FROM playlist_table where id = :playlistId")
    suspend fun deleteById(playlistId: Int)

    @Query("SELECT * FROM playlist_table where id = :playlistId")
    suspend fun getOne(playlistId: Int) : PlaylistEntity?
    @Query("update playlist_table set trackIds = :trackIds where id = :playlistId")
    suspend fun updateTrackIds(trackIds: String, playlistId: Int)

}