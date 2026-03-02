package dom.dima.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dom.dima.practicum.playlistmaker.media.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOne(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table order by created_ts desc")
    fun getAll(): Flow<List<PlaylistEntity>>

    @Query("DELETE FROM playlist_table where id = :playlistId")
    suspend fun deleteById(playlistId: Int)

    @Query("SELECT * FROM playlist_table where id = :playlistId")
    suspend fun getOne(playlistId: Int) : PlaylistEntity?



}