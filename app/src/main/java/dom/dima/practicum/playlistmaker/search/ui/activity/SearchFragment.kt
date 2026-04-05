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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentSearchBinding
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.state.SearchState
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class SearchFragment : Fragment(), ApplicationConstants {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    private val tracks = mutableListOf<Track>()
    private lateinit var trackAdapter: TrackAdapter

    private var inputSearchText: String = DEFAULT_STR
    private var searchTrack: String = ""

    companion object {
        private const val DEFAULT_STR = ""
    }

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
//        _binding = FragmentSearchBinding.inflate(inflater, container, false)
//        return binding.root
    }

/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        hideAllViews()
//        initializeViews()
//        setupRecyclerView()
//        setupSearchEditText()
//        setupObservers()
//        setupClickListeners()
    }

    private fun initializeViews() {
        hideAllViews()
    }

    private fun setupRecyclerView() {
        trackAdapter = TrackAdapter(
            tracks,
            viewModel,
            findNavController(),

        )
        binding.trackRecyclerView.adapter = trackAdapter
    }

    private fun setupSearchEditText() {
        with(binding.searchEditText) {
            setOnFocusChangeListener { _, hasFocus ->
                onSearchFocusChanged(hasFocus)
            }

            addTextChangedListener(createTextWatcher())

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideAllViews()
                    viewModel.doSearch(searchTrack)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun onSearchFocusChanged(hasFocus: Boolean) {
        hideAllViews()
        if (hasFocus) {
            viewModel.loadHistoryTracks()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchHistory(historyTracks: List<Track>) {

        if (!isAdded || view == null) return

        binding.progressBar.isVisible = false

        if (historyTracks.isNotEmpty() && binding.searchEditText.hasFocus()) {
            tracks.clear()

            binding.trackRecyclerView.isVisible = true
            binding.clearHistory.isVisible = true
            binding.searchHistoryText.isVisible = true

            tracks.addAll(historyTracks)
            trackAdapter.notifyDataSetChanged()
        }
    }

    private fun createTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            searchTrack = s.toString().trim()
            binding.clearIcon.isVisible = s?.isNotEmpty() == true

            if (s.isNullOrEmpty()) {
                viewModel.loadHistoryTracks()
                return
            }
            if (viewModel.performedSearchStr != searchTrack) {
                hideAllViews()
                viewModel.scheduleSearch(searchTrack)
            }
        }

        override fun afterTextChanged(s: Editable?) {
            inputSearchText = s?.toString() ?: DEFAULT_STR
        }
    }

    private fun setupObservers() {
        viewModel.getState().observe(viewLifecycleOwner) { state ->

            if (isAdded && view != null) {
                render(state)
            }
        }
    }

    private fun setupClickListeners() {
        binding.clearHistory.setOnClickListener {
            clearSearchHistory()
        }

        binding.clearIcon.setOnClickListener {
            binding.searchEditText.setText("")
            viewModel.loadHistoryTracks()
        }

        binding.btnReload.setOnClickListener {
            hideAllViews()
            viewModel.doSearch(searchTrack)
        }
    }

    private fun clearSearchHistory() {
        tracks.clear()
        viewModel.clearHistory()
        hideAllViews()
    }


    private fun render(state: SearchState) {

        if (!isAdded || view == null) return

        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Error -> showError(state.message)
            is SearchState.NoInternet -> showNoConnectionView()
            is SearchState.Content -> showSearchResults(state.data)
            is SearchState.History -> showSearchHistory(state.data)
        }
    }

    private fun showNoConnectionView() {
        hideAllViews()
        binding.noConnect.isVisible = true

    }

    private fun showLoading() {
        hideAllViews()
        binding.progressBar.isVisible = true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResults(foundTracks: List<Track>) {

        if (!isAdded || view == null) return

        if (foundTracks.isNotEmpty()) {
            tracks.clear()
            tracks.addAll(foundTracks)
            hideAllViews()
            binding.trackRecyclerView.isVisible = true
            trackAdapter.notifyDataSetChanged()
        } else {
            hideAllViews()
            binding.noContent.isVisible = true
        }

    }

    private fun showError(message: String) {
        if (!isAdded || view == null) return

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

        hideAllViews()
        binding.btnReload.isVisible = true
    }

    private fun hideAllViews() {
        with(binding) {
            trackRecyclerView.isVisible = false
            noContent.isVisible = false
            clearHistory.isVisible = false
            searchHistoryText.isVisible = false
            progressBar.isVisible = false

            btnReload.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
*/
}

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel
) {

    val state by viewModel.getState().observeAsState(initial = SearchState.History(emptyList()))

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // Toolbar
        SearchToolbar()
        Spacer(modifier = Modifier.height(8.dp))

        // Search Field
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
            }
        )

        // Content
        when (val currentState = state) {
            is SearchState.Loading -> LoadingContent()
            is SearchState.Error -> ErrorContent(
                message = currentState.message,
                onRetry = { viewModel.doSearch(searchText.text.trim()) }
            )
            is SearchState.NoInternet -> NoInternetContent(
                onRetry = { viewModel.doSearch(searchText.text.trim()) }
            )
            is SearchState.Content -> SearchResultsContent(
                tracks = currentState.data,
                viewModel = viewModel,
                navController = navController
            )
            is SearchState.History -> SearchHistoryContent(
                tracks = currentState.data,
                viewModel = viewModel,
                navController = navController,
                onClearHistory = { viewModel.clearHistory() }
            )
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
//                color = Color(0xFF6200EE),
                fontSize = 22.sp
            )
        },
        backgroundColor = colorResource(R.color.bkg_window_color_),
//        backgroundColor = Color(0xFF6200EE),
        elevation = 0.dp
    )
}

@Composable
fun SearchField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onClear: () -> Unit,
    onSearch: () -> Unit
) {
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
                .padding(horizontal = 16.dp, vertical = 10.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search),
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
                                imageVector = ImageVector.vectorResource(id = R.drawable.clear),
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
            // В реальном проекте добавьте иконку ошибки
            Text(
                text = message,
                textAlign = TextAlign.Center,
                color = Color.Red
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
                painter = painterResource(R.drawable.ic_no_connect),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.no_connect),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
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
    if (tracks.isEmpty()) {
        EmptyContent()
    } else {
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
}

@Composable
fun SearchHistoryContent(
    tracks: List<Track>,
    viewModel: SearchViewModel,
    navController: NavController,
    onClearHistory: () -> Unit
) {
    if (tracks.isEmpty()) {
        // Пустое состояние истории
        Box(modifier = Modifier.fillMaxSize())
    } else {
        Column {
            Text(
                text = stringResource(R.string.search_history),
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
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

            Button(
                onClick = onClearHistory,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
            ) {
                Text(stringResource(R.string.clear_history))
            }
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
                painter = painterResource(R.drawable.ic_no_content),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(R.string.nothing_to_find),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun TrackItem(
    track: Track,
    viewModel: SearchViewModel,
    navController: NavController
) {
    var clickAllowed by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = clickAllowed) {
                if (clickAllowed) {
                    clickAllowed = false
                    viewModel.clickDebounce()
                    viewModel.addToHistory(track)

                    // Навигация в плеер
                    val trackJson = viewModel.gson()?.toJson(track)
                    navController.navigate(
                        "player/$trackJson"
                    )

                    // Возвращаем возможность клика после дебаунса
                    androidx.compose.runtime.snapshots.SnapshotStateList<Long>()
                    // Лучше использовать корутину с задержкой
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Обложка трека
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.artworkUrl100)
                .crossfade(true)
                .build(),
            contentDescription = track.trackName,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_no_image_placeholder_45)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Информация о треке
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = track.trackName,
                fontSize = 16.sp,
                maxLines = 1
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (track.artistName != null) Text(
                    text = track.artistName,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "•",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(track.trackTimeMillis),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        // Иконка перехода
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
