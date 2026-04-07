package dom.dima.practicum.playlistmaker.media.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.PlaylistEditorFragment
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.playlist_screen.PlaylistScreenFragment
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistsViewModel

@Composable
fun PlaylistsContent(
    navController: NavController,
    viewModel: PlaylistsViewModel
) {
    val playlists by viewModel.getPlaylistsState().observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.initPlaylistsList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                navController.navigate(
                    R.id.action_mediaFragment_to_editPlaylistFragment2,
                    PlaylistEditorFragment.createArgs(PlaylistEditorFragment.NEW_PLAYLIST_MARKER)
                )
            },
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(R.color.btn_activity_search_color),
                contentColor = colorResource(R.color.btn_reload_text)
            ),
            shape = RoundedCornerShape(54.dp)
        ) {
            Text(
                text = stringResource(R.string.new_playlist),
                fontFamily = YsDisplayMedium,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (playlists.isEmpty()) {
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
                    text = stringResource(R.string.no_any_playlist),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.infos_text_color),
                    fontFamily = YsDisplayMedium
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(playlists, key = { it.id }) { playlist ->
                    PlaylistGridItem(
                        playlist = playlist,
                        onClick = {
                            navController.navigate(
                                R.id.action_mediaFragment_to_playlistScreenFragment,
                                PlaylistScreenFragment.createArgs(playlist.id)
                            )
                        }
                    )
                }
            }
        }
    }
}
