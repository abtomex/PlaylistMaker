package dom.dima.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import dom.dima.practicum.playlistmaker.data.Track
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects

class AudioplayerActivity : ApplicationConstants, AbstractButtonBackActivity()  {

    val gson: Gson = Gson()

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
            .load(track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .fitCenter()
            .placeholder(R.drawable.ic_no_image_placeholder_45)
            .transform(RoundedCorners(dpToPx(8.0f, this)))
            .into(trackIcon)
        val durability = findViewById<TextView>(R.id.durability_val)
        durability.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        setText("1:23", null, findViewById(R.id.progress))
        setText(track.trackName,null, findViewById(R.id.track_name))
        setText(track.artistName, null, findViewById(R.id.artist_name))
        setText(track.collectionName, findViewById(R.id.album), findViewById(R.id.album_val))
        setText(track.primaryGenreName, findViewById(R.id.genre), findViewById(R.id.genre_val))
        setText(track.country, findViewById(R.id.country), findViewById(R.id.country_val))

        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = inputFormat.parse(track.releaseDate)
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


    }

    private fun setText(text: String, key: TextView?, view: TextView?) {
        view?.text = text
        if(Objects.isNull(text) || text.isEmpty()) {
            view?.visibility = View.GONE
            key?.visibility = View.GONE
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

}