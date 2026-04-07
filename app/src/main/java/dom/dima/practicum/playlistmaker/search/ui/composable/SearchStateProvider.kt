package dom.dima.practicum.playlistmaker.search.ui.composable

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.state.SearchState
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class SearchStateProvider : PreviewParameterProvider<SearchState> {

    override val values = sequenceOf(
        SearchState.Content(
            listOf(
                createMockTrack(1, "Test Track 1", "Artist 1"),
                createMockTrack(2, "Test Track 2", "Artist 2")
            )
        ),
        SearchState.History(
            listOf(
                createMockTrack(3, "History Track", "History Artist")
            )
        ),
        SearchState.Loading,
        SearchState.NoInternet("no internet"),
        SearchState.Error("Something went wrong")
    )

    private fun createMockTrack(id: Int, name: String, artist: String) = Track(
        trackId = id,
        trackName = name,
        artistName = artist,
        trackTimeMillis = 180000,
        trackTimeStr = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(180000),
        artworkUrl100 = "https://via.placeholder.com/100",
        isFavorite = false,
        collectionName = "TODO()",
        releaseDate = "TODO()",
        primaryGenreName = "TODO()",
        country = "TODO()",
        previewUrl = "TODO()"
    )
}

@Preview(
    showBackground = true,
    name = "Search Screen - Content State"
)
@Composable
fun SearchScreenPreviewWithState(
    @PreviewParameter(SearchStateProvider::class) state: SearchState
) {
    MaterialTheme {
        val mockNavController = rememberNavController()

        SearchScreen(
            navController = mockNavController,
            viewModel = MockSearchViewModel(state)
        )
    }
}

class MockSearchViewModel(initialState: SearchState) : SearchViewModel(null, null) {
    private val _state = MutableLiveData(initialState)

    override fun getState() = _state

    override fun doSearch(searchTrack: String) = Unit
    override fun scheduleSearch(searchTrack: String) = Unit
    override fun loadHistoryTracks() = Unit
    override fun addToHistory(track: Track) = Unit
    override fun clearHistory() = Unit
    override fun clickDebounce() = Unit
}