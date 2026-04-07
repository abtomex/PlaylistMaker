package dom.dima.practicum.playlistmaker.search.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel

@Composable
fun SearchHistoryContent(
    tracks: List<Track>,
    viewModel: SearchViewModel,
    navController: NavController,
    onClearHistory: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.search_history),
            fontSize = 18.sp,
            color = colorResource(R.color.infos_text_color),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(tracks, key = { it.trackId }) { track ->
                TrackItem(
                    track = track,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }

        ClearHistoryButton(Modifier
            .fillMaxWidth()
            .weight(1f),
            onClearHistory)
    }
}
