package dom.dima.practicum.playlistmaker.data.network

import dom.dima.practicum.playlistmaker.domain.api.ApiResponse
import retrofit2.Response

fun <T, V> Response<T>.mapToApiResponse(mapSuccess: (dto: T) -> V): ApiResponse<V> {
    val dto = body()
    val errorMessage = "Error during loading data"

    return when {
        !isSuccessful -> ApiResponse.Error(errorBody()?.string() ?: errorMessage)
        dto != null -> ApiResponse.Success(mapSuccess(dto))
        else -> ApiResponse.Error(errorMessage)
    }
}
