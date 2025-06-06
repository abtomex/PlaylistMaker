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
import androidx.recyclerview.widget.RecyclerView
import dom.dima.practicum.playlistmaker.api.SearchTrackApi
import dom.dima.practicum.playlistmaker.data.Track
import dom.dima.practicum.playlistmaker.data.TracksResponse
import dom.dima.practicum.playlistmaker.rvcomponents.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AbstractButtonBackActivity() {

    private var inputSearchText: String = DEFAULT_STR

    private val imdbBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackApiService = retrofit.create(SearchTrackApi::class.java)
    private var trackAdapter: TrackAdapter = TrackAdapter(tracks)
    private var searchTrack: String = ""

    override fun buttonBackId(): Int {
        return R.id.search_layout
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val recyclerView = initSongsRecyclerView()
        val noContentView = findViewById<LinearLayout>(R.id.no_content)
        val noConnectView = findViewById<LinearLayout>(R.id.no_connect)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener {

            searchEditText.text.clear()
            searchEditText.clearFocus()
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(searchEditText.windowToken, 0)
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            allGone(recyclerView, noConnectView, noContentView)
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

        val apiCallback = initApiCallback(recyclerView, noConnectView, noContentView)

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

    private fun allGone(
        recyclerView: RecyclerView,
        noConnectView: LinearLayout,
        noContentView: LinearLayout
    ) {
        recyclerView.visibility = View.GONE
        noConnectView.visibility = View.GONE
        noContentView.visibility = View.GONE
    }

    private fun initApiCallback(
        recyclerView: RecyclerView,
        noConnectView: LinearLayout,
        noContentView: LinearLayout
    ): Callback<TracksResponse> {
        return (object : Callback<TracksResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                if (response.code() == 200) {
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        allGone(recyclerView, noConnectView, noContentView)
                        recyclerView.visibility = View.VISIBLE

                        tracks.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                    } else {
                        allGone(recyclerView, noConnectView, noContentView)
                        noContentView.visibility = View.VISIBLE

                    }
                } else {
                    allGone(recyclerView, noConnectView, noContentView)
                    noContentView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                allGone(recyclerView, noConnectView, noContentView)
                noConnectView.visibility = View.VISIBLE
            }
        })
    }

    private fun initSongsRecyclerView() : RecyclerView {
        val trackRecyclerView = findViewById<RecyclerView>(R.id.trackRecyclerView)

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

        val tracks = ArrayList<Track>()
    }

}
