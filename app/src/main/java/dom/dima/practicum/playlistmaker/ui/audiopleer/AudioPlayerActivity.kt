package dom.dima.practicum.playlistmaker.ui.audiopleer

import android.content.Context
import android.media.MediaPlayer
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
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.ApplicationConstants
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.domain.models.Track
import dom.dima.practicum.playlistmaker.ui.AbstractButtonBackActivity
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects

class AudioPlayerActivity : ApplicationConstants, AbstractButtonBackActivity() {

    private val gson: Gson = Gson()
    private lateinit var commonButton: ImageButton
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val handler = Handler(Looper.getMainLooper())

    override fun buttonBackId(): Int {
        return R.id.header
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val trackJson = intent.getStringExtra(CLICKED_TRACK_CONTENT)
        val track = gson.fromJson(trackJson, Track::class.java)

        val trackIcon = findViewById<ImageView>(R.id.cover)
        Glide.with(this)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .fitCenter()
            .placeholder(R.drawable.ic_no_image_placeholder_45)
            .transform(RoundedCorners(dpToPx(8.0f, this)))
            .into(trackIcon)
        val durability = findViewById<TextView>(R.id.durability_val)
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

        preparePlayer(track.previewUrl)
        commonButton = findViewById(R.id.common_button)
        commonButton.setOnClickListener {
            playbackControl()
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

    private fun preparePlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            commonButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            handler.removeCallbacks(progressRunnable)
            isStarted = false
            commonButton.setImageResource(R.drawable.button_play)
            playerState = STATE_PREPARED
            setText("00:00", null, findViewById(R.id.progress))
        }

    }

    private var isStarted: Boolean = false


    private val progressRunnable = object : Runnable {
        override fun run() {
            setText(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition), null, findViewById(R.id.progress)
            )
            handler.postDelayed(this, DELAY)
        }

    }

    private fun startPlayer() {
        mediaPlayer.start()
        commonButton.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
        isStarted = true

        handler.postDelayed(progressRunnable, DELAY)
    }

    private fun pausePlayer() {
        handler.removeCallbacks(progressRunnable)
        isStarted = false
        mediaPlayer.pause()
        commonButton.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {

        super.onDestroy()
        pausePlayer()
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 500L
    }

}