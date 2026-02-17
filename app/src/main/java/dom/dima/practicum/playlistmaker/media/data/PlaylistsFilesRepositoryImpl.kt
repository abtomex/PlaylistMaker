package dom.dima.practicum.playlistmaker.media.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dom.dima.practicum.playlistmaker.media.domain.PlaylistsFilesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistsFilesRepositoryImpl (
    private val context: Context
) : PlaylistsFilesRepository {

    override suspend fun createPlaylistCover(uri: Uri): Flow<String> = flow {
        val filePath = File( context.filesDir, "pm")
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = UUID.randomUUID().toString()
        val file = File(filePath, fileName)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        emit(fileName)
    }

}