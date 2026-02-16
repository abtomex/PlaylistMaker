package dom.dima.practicum.playlistmaker.media.domain.db

import dom.dima.practicum.playlistmaker.media.domain.state.AddToDbState
import kotlinx.coroutines.flow.Flow

interface AbstractMediaInteractor<T> {
    fun getAll(): Flow<List<T?>>
    suspend fun addOne(t: T): Flow<AddToDbState<T>>

}