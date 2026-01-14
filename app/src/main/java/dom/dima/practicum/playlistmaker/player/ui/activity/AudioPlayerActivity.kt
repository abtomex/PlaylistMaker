package dom.dima.practicum.playlistmaker.player.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dom.dima.practicum.playlistmaker.AbstractButtonBackActivity
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.player.ui.state.AudioPlayerState
import dom.dima.practicum.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import dom.dima.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects

class AudioPlayerActivity : ApplicationConstants, AbstractButtonBackActivity() {

    private lateinit var commonButton: ImageButton

    private var playerState = AudioPlayerViewModel.STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())

    private val viewModel by viewModel<AudioPlayerViewModel>()

    override fun buttonBackId(): Int {
        return R.id.header
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val trackJson = intent.getStringExtra(CLICKED_TRACK_CONTENT)
        val track = viewModel.fromJson(trackJson, Track::class.java)
        val trackIcon = findViewById<ImageView>(R.id.cover)
        val durability = findViewById<TextView>(R.id.durability_val)
        commonButton = findViewById(R.id.common_button)

        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .fitCenter()
            .placeholder(R.drawable.ic_no_image_placeholder_45)
            .transform(RoundedCorners(dpToPx(8.0f, this)))
            .into(trackIcon)
        durability.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        setText("00:00", null, findViewById(R.id.progress))
        setText(track.trackName, null, findViewById(R.id.track_name))
        setText(track.artistName, null, findViewById(R.id.artist_name))
        setText(track.collectionName, findViewById(R.id.album), findViewById(R.id.album_val))
        setText(track.primaryGenreName, findViewById(R.id.genre), findViewById(R.id.genre_val))
        setText(track.country, findViewById(R.id.country), findViewById(R.id.country_val))

        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = track.releaseDate?.let { inputFormat.parse(it) }
            val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            setText(
                outputFormat.format(date!!),
                findViewById(R.id.year),
                findViewById(R.id.year_val)
            )
        } catch (exc: Exception) {
            setText(
                "",
                findViewById(R.id.year),
                findViewById(R.id.year_val)
            )
        }

        viewModel.preparePlayer(track.previewUrl)

        commonButton.setOnClickListener {
            playbackControl()
        }

        viewModel.getState().observe(this) { state ->
            playerState = state.stateData.playerState
            when (state) {
                is AudioPlayerState.Prepared -> {
                    commonButton.isEnabled = true
                }

                is AudioPlayerState.Completion -> {
                    handler.removeCallbacks(progressRunnable)
                    isStarted = false
                    commonButton.setImageResource(R.drawable.button_play)
                    setText("00:00", null, findViewById(R.id.progress))
                }

                is AudioPlayerState.Start -> {
                    commonButton.setImageResource(R.drawable.button_pause)
                    isStarted = true
                    handler.postDelayed(progressRunnable, DELAY)

                }

                is AudioPlayerState.Pause -> {
                    handler.removeCallbacks(progressRunnable)
                    isStarted = false
                    commonButton.setImageResource(R.drawable.button_play)

                }
            }


        }

    }

    private fun setText(text: String?, key: TextView?, view: TextView?) {
        view?.text = text
        if (Objects.isNull(text) || text.isNullOrEmpty()) {
            view?.isVisible = false
            key?.isVisible = false
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private var isStarted: Boolean = false


    private val progressRunnable = object : Runnable {
        override fun run() {
            setText(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(viewModel.currentPosition()), null, findViewById(R.id.progress)
            )
            handler.postDelayed(this, DELAY)
        }

    }

    private fun playbackControl() {
        when (playerState) {
            AudioPlayerViewModel.STATE_PLAYING -> {
                viewModel.pausePlayer()
            }

            AudioPlayerViewModel.STATE_PREPARED, AudioPlayerViewModel.STATE_PAUSED -> {
                viewModel.startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {

        super.onDestroy()
        viewModel.pausePlayer()
        viewModel.releasePlayer()
    }

    companion object {
        private const val DELAY = 500L

    }
}