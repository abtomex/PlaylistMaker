package dom.dima.practicum.playlistmaker.settings.domain.api

sealed interface ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>
    data class Error<T>(val message: String) : ApiResponse<T>
}