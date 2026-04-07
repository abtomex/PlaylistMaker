package dom.dima.practicum.playlistmaker.media.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.ui.fragment.media.YsDisplayMedium
import dom.dima.practicum.playlistmaker.media.ui.fragment.media.YsDisplayRegular
import dom.dima.practicum.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import dom.dima.practicum.playlistmaker.media.ui.view_model.PlaylistsViewModel
import kotlinx.coroutines.launch

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
            .statusBarsPadding()
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
                            fontFamily = YsDisplayRegular,
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

