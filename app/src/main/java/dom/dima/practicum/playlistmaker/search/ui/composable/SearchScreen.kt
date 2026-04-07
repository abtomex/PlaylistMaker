package dom.dima.practicum.playlistmaker.search.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.search.ui.state.SearchState
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel

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
            .statusBarsPadding()
            .systemBarsPadding()
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
