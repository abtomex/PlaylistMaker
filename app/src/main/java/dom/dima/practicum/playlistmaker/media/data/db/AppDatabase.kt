package dom.dima.practicum.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dom.dima.practicum.playlistmaker.media.data.db.dao.TracksDao
import dom.dima.practicum.playlistmaker.media.data.db.dao.PlaylistsDao
import dom.dima.practicum.playlistmaker.media.data.db.entity.TrackEntity
import dom.dima.practicum.playlistmaker.media.data.db.entity.PlaylistEntity

@Database(version = 4, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun tracksDao() : TracksDao
    abstract fun playlistsDao() : PlaylistsDao

}