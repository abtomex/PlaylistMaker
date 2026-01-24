package dom.dima.practicum.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentSearchBinding
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import dom.dima.practicum.playlistmaker.search.ui.state.SearchState
import dom.dima.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), ApplicationConstants {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    private var inputSearchText: String = DEFAULT_STR
    private val tracks = mutableListOf<Track>()
    private var searchTrack: String = ""

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryService: SearchHistoryService

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable = createSearchRunnable(emptyList())

    @Volatile
    private var searchInProgress = false
    @Volatile
    private var searchIsScheduled = false

    companion object {
        private const val DEFAULT_STR = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        setupRecyclerView()
        setupSearchEditText()
        setupObservers()
        setupClickListeners()
    }

    private fun initializeViews() {
        hideAllViews()
    }

    private fun setupRecyclerView() {
        val sharedPreferences = requireContext()
            .getSharedPreferences(APPLICATION_PREFERENCES, MODE_PRIVATE)

        searchHistoryService = SearchHistoryService(
            sharedPreferences,
            viewModel.gson()
        )

        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (TRACK_HISTORY == key) {
                updateTrackHistory()
            }
        }

        trackAdapter = TrackAdapter(
            tracks,
            searchHistoryService,
            viewModel.gson(),
            findNavController()
        )

        binding.trackRecyclerView.adapter = trackAdapter
    }

    private fun updateTrackHistory() {
        tracks.clear()
        tracks.addAll(searchHistoryService.tracks)
        hideAllViews()

        binding.trackRecyclerView.isVisible = true
        binding.searchHistoryText.isVisible = true
        binding.clearHistory.isVisible = true

        trackAdapter.notifyDataSetChanged()
    }

    private fun setupSearchEditText() {
        with(binding.searchEditText) {
            setOnFocusChangeListener { _, hasFocus ->
                onSearchFocusChanged(hasFocus)
            }

            addTextChangedListener(createTextWatcher())

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    performSearch()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun onSearchFocusChanged(hasFocus: Boolean) {
        hideAllViews()
        if (hasFocus && searchHistoryService.tracks.isNotEmpty()) {
            showSearchHistory()
        }
    }

    private fun showSearchHistory() {
        tracks.clear()
        tracks.addAll(searchHistoryService.tracks)
        trackAdapter.notifyDataSetChanged()

        binding.trackRecyclerView.isVisible = true
        binding.clearHistory.isVisible = true
        binding.searchHistoryText.isVisible = true
    }

    private fun createTextWatcher() = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            searchTrack = s.toString().trim()
            binding.clearIcon.isVisible = s?.isNotEmpty() == true

            if (s.isNullOrEmpty()) {
                handler.removeCallbacks(searchRunnable)
                searchIsScheduled = false
                showSearchHistoryIfNeeded()
            } else {
                scheduleSearch()
            }
        }

        override fun afterTextChanged(s: Editable?) {
            inputSearchText = s?.toString() ?: DEFAULT_STR
        }
    }

    private fun scheduleSearch() {
        handler.removeCallbacks(searchRunnable)
        if (!searchIsScheduled) {
            searchIsScheduled = true
            handler.postDelayed(
                { performSearch() },
                SEARCH_DEBOUNCE_DELAY
            )
        }
    }

    private fun showSearchHistoryIfNeeded() {
        binding.searchEditText.text?.clear()
        binding.searchEditText.clearFocus()
        hideKeyboard()

        tracks.clear()
        if (searchHistoryService.tracks.isNotEmpty()) {
            tracks.addAll(searchHistoryService.tracks)
            hideAllViews()

            binding.searchHistoryText.isVisible = true
            binding.trackRecyclerView.isVisible = true
            binding.clearHistory.isVisible = true
        }

        trackAdapter.notifyDataSetChanged()
    }

    private fun setupObservers() {
        viewModel.getState().observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun setupClickListeners() {
        binding.clearHistory.setOnClickListener {
            clearSearchHistory()
        }

        binding.clearIcon.setOnClickListener {
            showSearchHistoryIfNeeded()
        }

        binding.btnReload.setOnClickListener {
            performSearch()
        }

        binding.actionBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun clearSearchHistory() {
        tracks.clear()
        searchHistoryService.clearHistory()
        hideAllViews()
    }

    private fun performSearch() {
        if (searchTrack.isEmpty() || searchInProgress) {
            searchIsScheduled = false
            return
        }

        searchInProgress = true
        hideAllViews()

        viewModel.loadData(searchTrack)
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Error -> showError(state.message)
            is SearchState.Content -> showSearchResults(state.data)
        }
    }

    private fun showLoading() {
        hideAllViews()
        binding.progressBar.isVisible = true
    }

    private fun showSearchResults(foundTracks: List<Track>) {
        handler.removeCallbacks(searchRunnable)

        val newSearchRunnable = createSearchRunnable(foundTracks)
        searchRunnable = newSearchRunnable
        handler.post(newSearchRunnable)

        searchIsScheduled = false
        searchInProgress = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createSearchRunnable(foundTracks: List<Track>): Runnable {
        return Runnable {
            binding.progressBar.isVisible = false

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
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun hideAllViews() {
        with(binding) {
            trackRecyclerView.isVisible = false
            noContent.isVisible = false
            clearHistory.isVisible = false
            searchHistoryText.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val view = view?.findViewById<View>(R.id.searchEditText) ?: view
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(searchRunnable)
        _binding = null
    }
}