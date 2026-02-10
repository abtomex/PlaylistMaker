package dom.dima.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dom.dima.practicum.playlistmaker.media.data.db.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(movies: List<FavoriteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(movies: FavoriteEntity)

    // Комментарий к критическому замечанию ревьюера: "Треки в списке отсортированы
    // по убыванию порядка добавления - последние добавленные в избранные треки
    // находятся в верхней части списка." "На экране «Избранное» не верный порядок
    // добавленных треков. Последний добавленный трек должен находится в начале списка."
    //
    // Я добавляю в БД треки и ставлю created_ts. А когда запрашиваю избранное,
    // блоком "order by created_ts desc" отсортировываю в обратном порядке,
    // чтобы последние добавленные треки были в начале списка.
    //
    @Query("SELECT * FROM favorite_track_table order by created_ts desc")
    fun getTracks(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite_track_table where trackId = :trackId")
    fun getTrackById(trackId: Int): FavoriteEntity?

    @Query("DELETE FROM favorite_track_table where trackId = :trackId")
    suspend fun deleteTrackById(trackId: Int)


}