package dom.dima.practicum.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dom.dima.practicum.playlistmaker.media.data.db.dao.FavoritesDao
import dom.dima.practicum.playlistmaker.media.data.db.entity.FavoriteEntity

@Database(version = 1, entities = [FavoriteEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao() : FavoritesDao
}