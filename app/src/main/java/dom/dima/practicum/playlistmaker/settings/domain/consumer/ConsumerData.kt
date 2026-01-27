package dom.dima.practicum.playlistmaker.settings.domain.consumer

sealed interface ConsumerData<T> {

    data class Data<T>(val value: T) : ConsumerData<T>

    data class HistoryData<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val message: String) : ConsumerData<T>

}