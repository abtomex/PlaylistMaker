package dom.dima.practicum.playlistmaker.media.ui.fragment.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.domain.models.Playlist
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.PlaylistEditorFragment
import dom.dima.practicum.playlistmaker.media.ui.fragment.playlists.playlist_screen.PlaylistScreenFragment
import dom.dima.practicum.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistsViewModel
import dom.dima.practicum.playlistmaker.player.ui.activity.AudioPlayerFragment
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.utils.Useful
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import org.koin.androidx.viewmodel.ext.android.viewModel

val YsDisplayMedium = FontFamily(
    Font(R.font.ys_display_medium)
)

val YsDisplayRegular = FontFamily(
    Font(R.font.ys_display_regular)
)

class MediaFragment : Fragment() {

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()
    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MediaScreen(
                    navController = findNavController(),
                    favoriteTracksViewModel = favoriteTracksViewModel,
                    playlistsViewModel = playlistsViewModel
                )
            }
        }
    }
}

@Composable
fun MediaScreen(
    navController: NavController,
    favoriteTracksViewModel: FavoriteTracksViewModel,
    playlistsViewModel: PlaylistsViewModel
) {
    val pagerState = rememberPagerState(initialPage = 0) { 2 }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bkg_window_color_))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.media),
                    color = colorResource(R.color.text_color),
                    fontSize = 22.sp,
                    fontFamily = YsDisplayMedium
                )
            },
            backgroundColor = colorResource(R.color.bkg_window_color_),
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = colorResource(R.color.color_theme),
            contentColor = colorResource(R.color.text_color),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 2.dp,
                    color = colorResource(R.color.text_color)
                )
            }
        ) {
            listOf(
                stringResource(R.string.favorite_tracks),
                stringResource(R.string.playlists)
            ).forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            color = if (pagerState.currentPage == index)
                                colorResource(R.color.text_color)
                            else
                                colorResource(R.color.text_color).copy(alpha = 0.7f),
                            fontFamily = YsDisplayMedium,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> FavoriteTracksContent(
                    navController = navController,
                    viewModel = favoriteTracksViewModel
                )
                1 -> PlaylistsContent(
                    navController = navController,
                    viewModel = playlistsViewModel
                )
            }
        }
    }
}

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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavoriteTrackItem(
    track: Track,
    viewModel: FavoriteTracksViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (viewModel.clickIsAllowed) {
                    viewModel.clickIsAllowed = false
                    viewModel.clickDebounce {
                        navController.navigate(
                            R.id.action_mediaFragment_to_audioPlayerFragment,
                            AudioPlayerFragment.createArgs(viewModel.gson().toJson(track))
                        )
                    }
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = track.artworkUrl100,
            contentDescription = track.trackName,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.ic_no_image_placeholder_45),
            failure = placeholder(R.drawable.ic_no_image_placeholder_45)
        ) {
            it.transform(RoundedCorners(Useful.dpToPx(2f, context)))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                maxLines = 1,
                color = colorResource(R.color.track_text_color),
                fontFamily = YsDisplayRegular
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = track.artistName ?: "",
                    fontSize = 14.sp,
                    color = colorResource(R.color.track_text_artist_color),
                    maxLines = 1,
                    modifier = Modifier.weight(1f),
                    fontFamily = YsDisplayRegular
                )

                Text(
                    text = "•",
                    fontSize = 14.sp,
                    color = colorResource(R.color.track_text_artist_color),
                    fontFamily = YsDisplayRegular
                )

                Text(
                    text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(track.trackTimeMillis),
                    fontSize = 14.sp,
                    color = colorResource(R.color.track_text_artist_color),
                    fontFamily = YsDisplayRegular
                )
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_fwd_arrow_14),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(14.dp)
        )
    }

    Divider(
        color = Color.LightGray,
        modifier = Modifier.padding(start = 80.dp)
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaylistGridItem(
    playlist: Playlist,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val tracksCount = playlist.trackIds.size
    val tracksCountText = when {
        tracksCount % 10 == 1 && tracksCount % 100 != 11 -> "$tracksCount трек"
        tracksCount % 10 in 2..4 && (tracksCount % 100 !in 10..20) -> "$tracksCount трека"
        else -> "$tracksCount треков"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        GlideImage(
            model = playlist.cover,
            contentDescription = playlist.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.ic_no_image_placeholder_45),
            failure = placeholder(R.drawable.ic_no_image_placeholder_45)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = playlist.title,
            fontSize = 11.sp,
            maxLines = 1,
            color = colorResource(R.color.track_text_color),
            fontFamily = YsDisplayRegular
        )

        Text(
            text = tracksCountText,
            fontSize = 11.sp,
            maxLines = 1,
            color = colorResource(R.color.track_text_artist_color),
            fontFamily = YsDisplayRegular
        )
    }
}