package dom.dima.practicum.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dom.dima.practicum.playlistmaker.ui.AbstractButtonBackActivity
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.Creator
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.domain.api.TracksInteractor
import dom.dima.practicum.playlistmaker.domain.models.Track

class SearchActivity : ApplicationConstants, AbstractButtonBackActivity() {

    private var inputSearchText: String = DEFAULT_STR

    private var searchTrack: String = ""
    private val tracks = ArrayList<Track>()
    private var trackAdapter: TrackAdapter? = null
    private var searchHistoryService: SearchHistoryService? = null

    private var recyclerView: RecyclerView? = null
    private var noContentView: LinearLayout? = null
    private var noConnectView: LinearLayout? = null
    private var clearHistoryButton: Button? = null
    private var youSearchTitle: TextView? = null
    private var progressBar: ProgressBar? = null

    @Volatile
    private var searchIsScheduled = false
    @Volatile
    private var searchInProgress = false

    private var searchRunnable : Runnable = initSearchRunnable(emptyList())

    private val handler = Handler(Looper.getMainLooper())

    override fun buttonBackId(): Int {
        return R.id.search_layout
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = initSongsRecyclerView()
        noContentView = findViewById(R.id.no_content)
        noConnectView = findViewById(R.id.no_connect)
        clearHistoryButton = findViewById(R.id.clear_history)
        youSearchTitle = findViewById(R.id.search_history_text)
        progressBar = findViewById(R.id.progress_bar)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        this.clearHistoryButton?.setOnClickListener {
            tracks.clear()
            searchHistoryService?.clearHistory()
            allGone()
        }

        clearButton.setOnClickListener {
            showHistoryIfItsNeeded(searchEditText)
        }

        searchEditText.setOnFocusChangeListener { _, onFocus ->
            allGone()
            if (onFocus && searchHistoryService?.tracks?.isNotEmpty() == true) {
                tracks.clear()
                tracks.addAll(searchHistoryService!!.tracks)
                trackAdapter?.notifyDataSetChanged()
                recyclerView?.visibility = View.VISIBLE
                clearHistoryButton?.visibility = View.VISIBLE
                youSearchTitle?.visibility = View.VISIBLE

            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchTrack = s.toString().trim()
                if (!s.isNullOrEmpty()) {
                    println("!!!___кнопочки, thread=${Thread.currentThread().name}")
                    handler.removeCallbacks(searchRunnable)
                    if (!searchIsScheduled) {
                        searchIsScheduled = true
                        handler.postDelayed(
                            { doSearch() },
                            SEARCH_DEBOUNCE_DELAY
                        )
                    }
                    clearButton.visibility = View.VISIBLE
                } else {
                    clearButton.visibility = View.GONE
                    showHistoryIfItsNeeded(searchEditText)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                inputSearchText = s.toString()
            }

        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.removeCallbacks(searchRunnable)
                doSearch()
            }
            false
        }
        val buttonReload = findViewById<Button>(R.id.btn_reload)
        buttonReload.setOnClickListener {
            doSearch()
        }
    }

    private val tracksInteractor = Creator.provideTracksInteractor()

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistoryIfItsNeeded(searchEditText: EditText) {
        searchEditText.text.clear()
        searchEditText.clearFocus()
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(searchEditText.windowToken, 0)
        tracks.clear()
        if (searchHistoryService?.tracks?.isNotEmpty() == true) {
            tracks.addAll(searchHistoryService!!.tracks)
            allGone()
            youSearchTitle?.visibility = View.VISIBLE
            recyclerView?.visibility = View.VISIBLE
            clearHistoryButton?.visibility = View.VISIBLE
        }
        trackAdapter!!.notifyDataSetChanged()

    }

    private fun allGone() {
        recyclerView?.visibility = View.GONE
        noContentView?.visibility = View.GONE
        noConnectView?.visibility = View.GONE
        clearHistoryButton?.visibility = View.GONE
        youSearchTitle?.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSongsRecyclerView(): RecyclerView {
        val trackRecyclerView = findViewById<RecyclerView>(R.id.trackRecyclerView)

        val sharedPreferences = getSharedPreferences(APPLICATION_PREFERENCES, MODE_PRIVATE)
        searchHistoryService = SearchHistoryService(sharedPreferences)


        sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (TRACK_HISTORY == key) {
                tracks.clear()
                tracks.addAll(searchHistoryService!!.tracks)
                allGone()
                trackRecyclerView.visibility = View.VISIBLE
                youSearchTitle?.visibility = View.VISIBLE
                clearHistoryButton?.visibility = View.VISIBLE
                trackAdapter!!.notifyDataSetChanged()
            }
        }
        trackAdapter = TrackAdapter(tracks, searchHistoryService!!)
        trackRecyclerView.adapter = trackAdapter
        return trackRecyclerView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_TEXT, inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(SAVED_TEXT, DEFAULT_STR)
        findViewById<EditText>(R.id.searchEditText).setText(inputSearchText)
    }

    private fun doSearch() {
        println("!!!___$searchTrack, thread=${Thread.currentThread().name}")
        if (searchTrack.isEmpty()) {
            searchIsScheduled = false
            return
        }
        if (searchInProgress) return
        searchInProgress = true

        allGone()
        progressBar?.visibility = View.VISIBLE
        tracksInteractor.searchTracks(
            searchStr = searchTrack,
            consumer = object : TracksInteractor.TracksConsumer {
                @SuppressLint("NotifyDataSetChanged")
                override fun consume(foundTracks: List<Track>) {
                    println("!!!___ответ, thread=${Thread.currentThread().name}")
                    val currentRunnable = searchRunnable
                    handler.removeCallbacks(currentRunnable)
                    val newSearchRunnable : Runnable = initSearchRunnable(foundTracks)
                    searchRunnable = newSearchRunnable
                    handler.post(newSearchRunnable)
                    searchIsScheduled = false
                    searchInProgress = false
                }

            }
        )

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSearchRunnable(foundTracks: List<Track>): Runnable {
        return Runnable {
            progressBar?.visibility = View.GONE
            if (foundTracks.isNotEmpty()) {
                tracks.clear()
                allGone()
                recyclerView?.visibility = View.VISIBLE

                tracks.addAll(foundTracks)
                trackAdapter!!.notifyDataSetChanged()
            } else {
                allGone()
                noContentView?.visibility = View.VISIBLE

            }
        }
    }

    /*
        private val tracksConsumer: TracksInteractor.TracksConsumer = object : TracksInteractor.TracksConsumer {
            @SuppressLint("NotifyDataSetChanged")
            override fun consume(foundTracks: List<Track>) {

                val currentRunnable = searchRunnable
                handler.removeCallbacks(currentRunnable)

                val newSearchRunnable = Runnable {
                    progressBar?.visibility = View.GONE
                    if (foundTracks.isNotEmpty()) {
                        tracks.clear()
                        allGone()
                        recyclerView?.visibility = View.VISIBLE

                        tracks.addAll(foundTracks)
                        trackAdapter!!.notifyDataSetChanged()
                    } else {
                        allGone()
                        noContentView?.visibility = View.VISIBLE

                    }
                }
                searchRunnable = newSearchRunnable
                handler.post(newSearchRunnable)

            }

        }
    */


    companion object {
        private const val SAVED_TEXT = "SAVED_TEXT"
        private const val DEFAULT_STR = ""
        private const val SEARCH_DEBOUNCE_DELAY = 4000L
    }

}
