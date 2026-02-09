package dom.dima.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dom.dima.practicum.playlistmaker.media.data.db.entity.FavoriteEntity

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(movies: List<FavoriteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(movies: FavoriteEntity)

    @Query("SELECT * FROM favorite_track_table order by created_ts desc")
    suspend fun getTracks(): List<FavoriteEntity>

    @Query("SELECT * FROM favorite_track_table where trackId = :trackId")
    suspend fun getTrackById(trackId: Int): FavoriteEntity?

    @Query("DELETE FROM favorite_track_table where trackId = :trackId")
    suspend fun deleteTrackById(trackId: Int)


}