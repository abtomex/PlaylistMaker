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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener {
            searchEditText.text.clear()
            searchEditText.clearFocus()
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(searchEditText.windowToken, 0)
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

        val recyclerView = initSongsRecyclerView()
        val noContentView = findViewById<LinearLayout>(R.id.no_content)
        val noConnectView = findViewById<LinearLayout>(R.id.no_connect)

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
                        recyclerView.visibility = View.VISIBLE
                        noConnectView.visibility = View.GONE
                        noContentView.visibility = View.GONE

                        tracks.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                    } else {
                        noContentView.visibility = View.VISIBLE
                        noConnectView.visibility = View.GONE
                        recyclerView.visibility = View.GONE

                    }
                } else {
                    noContentView.visibility = View.GONE
                    noConnectView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                noContentView.visibility = View.GONE
                noConnectView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
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

        val tracks = mutableListOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                501,
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            Track(
                "Billie Jean",
                "Michael Jackson",
                435,
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),

            Track(
                "Stayin' Alive",
                "Bee Gees",
                410,
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),

            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                533,
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),

            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                503,
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg "
            ),
            Track(
                "Smells Like Teen Spirit Smells Like Teen Spirit Smells Like Teen Spirit",
                "Nirvana Smells Like Teen Spirit Smells Like Teen Spirit Nirvana Smells Like Teen Spirit Smells Like Teen Spirit",
                501,
                ""
            )

        )
    }

}
