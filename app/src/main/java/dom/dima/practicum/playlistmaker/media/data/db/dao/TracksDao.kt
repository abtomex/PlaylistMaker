package dom.dima.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dom.dima.practicum.playlistmaker.media.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(entities: List<TrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(entity: TrackEntity)

    @Query("SELECT * FROM track_table where is_favorite = 1 order by created_ts desc")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table where trackId = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity?

    @Query("DELETE FROM track_table where trackId = :trackId")
    suspend fun deleteTrackById(trackId: Int)
    @Query("SELECT * FROM track_table where trackId in (:ids)")
    suspend fun getTracksByIds(ids: Set<Int>) : List<TrackEntity>

    @Query("select * from track_table")
    suspend fun getAll() : List<TrackEntity>


}