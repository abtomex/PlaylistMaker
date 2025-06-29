package dom.dima.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dom.dima.practicum.playlistmaker.api.SearchTrackApi
import dom.dima.practicum.playlistmaker.data.Track
import dom.dima.practicum.playlistmaker.data.TracksResponse
import dom.dima.practicum.playlistmaker.rvcomponents.TrackAdapter
import dom.dima.practicum.playlistmaker.service.SearchHistoryService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : ApplicationConstants, AbstractButtonBackActivity() {

    private var inputSearchText: String = DEFAULT_STR

    private val imdbBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackApiService = retrofit.create(SearchTrackApi::class.java)
    private var searchTrack: String = ""
    private val tracks = ArrayList<Track>()
    private var trackAdapter: TrackAdapter? = null
    private var searchHistoryService: SearchHistoryService? = null

    private var recyclerView: RecyclerView? = null
    private var noContentView: LinearLayout? = null
    private var noConnectView: LinearLayout? = null
    private var clearHistoryButton: Button? = null
    private var youSearchTitle: TextView? = null

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

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        this.clearHistoryButton?.setOnClickListener {
            tracks.clear()
            searchHistoryService?.clearHistory()
            allGone()
        }

        clearButton.setOnClickListener {

            searchEditText.text.clear()
            searchEditText.clearFocus()
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(searchEditText.windowToken, 0)
            tracks.clear()
            if(searchHistoryService?.tracks?.isNotEmpty() == true) {
                tracks.addAll(searchHistoryService!!.tracks)
                allGone()
                youSearchTitle?.visibility = View.VISIBLE
                recyclerView?.visibility = View.VISIBLE
                clearHistoryButton?.visibility = View.VISIBLE
            }
            trackAdapter!!.notifyDataSetChanged()
        }

        searchEditText.setOnFocusChangeListener{ _, onFocus ->
            allGone()
            if(onFocus && searchHistoryService?.tracks?.isNotEmpty() == true) {
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
                if (!s.isNullOrEmpty()) {
                    clearButton.visibility = View.VISIBLE
                } else {
                    clearButton.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                inputSearchText = s.toString()
            }

        })

        val apiCallback = initApiCallback()

        searchEditText.setOnEditorActionListener { fieldSearch, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack = fieldSearch.text.toString().trim()
                trackApiService.search(searchTrack).enqueue(apiCallback)
            }
            false
        }
        val buttonReload = findViewById<Button>(R.id.btn_reload)
        buttonReload.setOnClickListener {
            trackApiService.search(searchTrack).enqueue(apiCallback)
        }
    }

    private fun allGone() {
        recyclerView?.visibility = View.GONE
        noContentView?.visibility = View.GONE
        noConnectView?.visibility = View.GONE
        clearHistoryButton?.visibility = View.GONE
        youSearchTitle?.visibility = View.GONE
    }

    private fun initApiCallback(): Callback<TracksResponse> {
        return (object : Callback<TracksResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                if (response.code() == 200) {
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        allGone()
                        recyclerView?.visibility = View.VISIBLE

                        tracks.addAll(response.body()?.results!!)
                        trackAdapter!!.notifyDataSetChanged()
                    } else {
                        allGone()
                        noContentView?.visibility = View.VISIBLE

                    }
                } else {
                    allGone()
                    noContentView?.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                allGone()
                noConnectView?.visibility = View.VISIBLE
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSongsRecyclerView() : RecyclerView {
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

    companion object {
        private const val SAVED_TEXT = "SAVED_TEXT"
        private const val DEFAULT_STR = ""

    }

}
