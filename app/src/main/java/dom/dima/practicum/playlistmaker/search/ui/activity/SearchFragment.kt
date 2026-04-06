package dom.dima.practicum.playlistmaker.search.ui.activity

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.player.ui.activity.AudioPlayerFragment
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.state.SearchState
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import dom.dima.practicum.playlistmaker.utils.Useful.Companion.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class SearchFragment : Fragment(), ApplicationConstants {

    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen(findNavController(), viewModel)
            }
        }
    }

}

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel()
) {
    val state by viewModel.getState().observeAsState(initial = SearchState.History(emptyList()))

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var firstLoaded by remember { mutableIntStateOf(0) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bkg_window_color_))
    ) {
        SearchToolbar()
        Spacer(modifier = Modifier.height(8.dp))
        SearchField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
                val trimmedText = newText.text.trim()
                if (trimmedText.isEmpty()) {
                    viewModel.loadHistoryTracks()
                } else {
                    viewModel.scheduleSearch(trimmedText)
                }
            },
            onClear = {
                searchText = TextFieldValue("")
                viewModel.loadHistoryTracks()
                focusManager.clearFocus()
            },
            onSearch = {
                focusManager.clearFocus()
                if (searchText.text.trim().isNotEmpty()) {
                    viewModel.doSearch(searchText.text.trim())
                }
            },
            onTapSearch = {
                if (firstLoaded == 0) {
                    firstLoaded++
                } else if (firstLoaded == 1) {
                    viewModel.loadHistoryTracks()
                    firstLoaded++
                }
            }
        )

        when (val currentState = state) {
            is SearchState.Loading -> LoadingContent()
            is SearchState.Error -> ErrorContent(
                message = currentState.message,
                onRetry = {
                    if (searchText.text.trim().isNotEmpty()) {
                        viewModel.doSearch(searchText.text.trim())
                    }
                }
            )

            is SearchState.NoInternet -> NoInternetContent(
                onRetry = {
                    if (searchText.text.trim().isNotEmpty()) {
                        viewModel.doSearch(searchText.text.trim())
                    }
                }
            )

            is SearchState.Content -> {
                if (currentState.data.isEmpty()) {
                    EmptyContent()
                } else {
                    SearchResultsContent(
                        tracks = currentState.data,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }

            is SearchState.History -> {
                if (currentState.data.isNotEmpty() && searchText.text.isEmpty()) {
                    SearchHistoryContent(
                        tracks = currentState.data,
                        viewModel = viewModel,
                        navController = navController,
                        onClearHistory = {
                            viewModel.clearHistory()
                        }
                    )
                } else if (currentState.data.isEmpty() && searchText.text.isEmpty()) {
                    Spacer(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Composable
fun SearchToolbar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.search),
                color = colorResource(R.color.text_color),
                fontSize = 22.sp
            )
        },
        backgroundColor = colorResource(R.color.bkg_window_color_),
        elevation = 0.dp
    )
}

@Composable
fun SearchField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onClear: () -> Unit,
    onSearch: () -> Unit,
    onTapSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(52.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF0F0F0),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .onFocusEvent { onTapSearch.invoke() },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSearch()
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )

                    Box(Modifier.weight(1f)) {
                        if (value.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.search),
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }

                    if (value.text.isNotEmpty()) {
                        IconButton(
                            onClick = onClear,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.clear),
                                contentDescription = "Clear",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF6200EE),
            modifier = Modifier.size(44.dp)
        )
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_no_connect),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.Unspecified
            )
            Text(
                text = message,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Gray
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
            ) {
                Text(stringResource(R.string.reload))
            }
        }
    }
}

@Composable
fun NoInternetContent(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_no_connect),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.no_connect),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = colorResource(R.color.infos_text_color)
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
            ) {
                Text(stringResource(R.string.reload))
            }
        }
    }
}

@Composable
fun SearchResultsContent(
    tracks: List<Track>,
    viewModel: SearchViewModel,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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
}

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
                .weight(2f), // 2 части из 3
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

@Preview
@Composable
fun PrevClearHistoryButton() {
    ClearHistoryButton(Modifier
        .fillMaxWidth()
    ) {}
}
@Composable
fun ClearHistoryButton(modifier: Modifier, onClearHistory: () -> Unit) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Button(
            onClick = onClearHistory,
            modifier = Modifier
                .padding(top = 16.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(R.color.btn_activity_search_color),
                contentColor = colorResource(R.color.btn_reload_text)
                ),
            shape = RoundedCornerShape(54.dp)
        ) {
            Text(stringResource(R.string.clear_history))
        }
    }

}

@Composable
fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_no_content),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.nothing_to_find),
                fontSize = 18.sp,
                color = colorResource(R.color.infos_text_color)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrackItem(
    track: Track,
    viewModel: SearchViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (viewModel.clickIsAllowed) {
                    viewModel.clickIsAllowed = false
                    viewModel.clickDebounce()
                    viewModel.addToHistory(track)

                    navController.navigate(
                        R.id.action_searchFragment_to_audioPlayerFragment,
                        AudioPlayerFragment.createArgs(viewModel.gson()?.toJson(track))
                    )
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
            it.transform(RoundedCorners(dpToPx(2.0f, context = context)))
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

                )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (track.artistName != null) Text(
                    text = track.artistName,
                    fontSize = 14.sp,
                    color = colorResource(R.color.track_text_artist_color),
                    maxLines = 1,
                )

                Text(
                    text = "•",
                    fontSize = 14.sp,
                    color = colorResource(R.color.track_text_artist_color),
                )

                Text(
                    text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(track.trackTimeMillis),
                    fontSize = 14.sp,
                    color = colorResource(R.color.track_text_artist_color),
                )
            }
        }

        Icon(
            painter = painterResource(R.drawable.ic_fwd_arrow_14),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
    }

    Divider(
        color = Color.LightGray,
        modifier = Modifier.padding(start = 80.dp)
    )
}

