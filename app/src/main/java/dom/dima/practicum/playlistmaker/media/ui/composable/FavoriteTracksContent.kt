package dom.dima.practicum.playlistmaker.media.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.ui.fragment.media.YsDisplayMedium
import dom.dima.practicum.playlistmaker.media.ui.view_model.FavoriteTracksViewModel

@Composable
fun FavoriteTracksContent(
    navController: NavController,
    viewModel: FavoriteTracksViewModel
) {
    val favoriteTracks by viewModel.getFavoriteState().observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (favoriteTracks.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_no_content),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.no_media),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.infos_text_color),
                    fontFamily = YsDisplayMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(favoriteTracks, key = { it.trackId }) { track ->
                    FavoriteTrackItem(
                        track = track,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}
