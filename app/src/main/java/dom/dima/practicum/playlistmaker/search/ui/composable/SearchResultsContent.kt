package dom.dima.practicum.playlistmaker.search.ui.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel

@Composable
fun SearchResultsContent(
    tracks: List<Track>,
    viewModel: SearchViewModel,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(tracks, key = { it.trackId }) { track ->
            TrackItem(
                track = track,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}