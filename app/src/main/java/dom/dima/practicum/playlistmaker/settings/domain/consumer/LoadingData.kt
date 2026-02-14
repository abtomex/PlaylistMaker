package dom.dima.practicum.playlistmaker.settings.domain.consumer

sealed interface LoadingData<T> {

    data class Data<T>(val value: T) : LoadingData<T>

    data class HistoryData<T>(val value: T) : LoadingData<T>
    data class Error<T>(val message: String) : LoadingData<T>
    data class NoInternet<T>(val message: String) : LoadingData<T>

}