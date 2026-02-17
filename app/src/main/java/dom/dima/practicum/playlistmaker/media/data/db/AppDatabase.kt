package dom.dima.practicum.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dom.dima.practicum.playlistmaker.media.data.db.dao.FavoritesDao
import dom.dima.practicum.playlistmaker.media.data.db.dao.PlaylistsDao
import dom.dima.practicum.playlistmaker.media.data.db.entity.FavoriteEntity
import dom.dima.practicum.playlistmaker.media.data.db.entity.PlaylistEntity

@Database(version = 3, entities = [FavoriteEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao() : FavoritesDao
    abstract fun playlistsDao() : PlaylistsDao

}