package dom.dima.practicum.playlistmaker.search.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentSearchBinding
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
                SearchToolbar()
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