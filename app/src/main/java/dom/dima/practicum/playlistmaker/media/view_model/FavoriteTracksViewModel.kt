package dom.dima.practicum.playlistmaker.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.media.domain.db.FavoritesInteractor
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val gson: Gson
) : ViewModel() {

    var clickIsAllowed: Boolean = true
    private val favoriteState = MutableLiveData<List<Track>>()
    fun getFavoriteState(): LiveData<List<Track>> = favoriteState


    fun loadFavorites() {
        viewModelScope.launch {
            favoritesInteractor.favoriteTracks()
                .collect { tracks ->
                    favoriteState.postValue(tracks)
                }
        }
    }

    fun clickDebounce(function: () -> Unit) {
        viewModelScope.launch {
            function.invoke()
            delay(CLICK_DEBOUNCE_DELAY)
            clickIsAllowed = true
        }

    }

    fun gson() : Gson {
        return this.gson
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}