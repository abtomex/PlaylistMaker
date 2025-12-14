package dom.dima.practicum.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.media.ui.MediaActivity
import dom.dima.practicum.playlistmaker.settings.ui.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            val displayIntent = Intent(this@MainActivity, dom.dima.practicum.playlistmaker.search.ui.activity.SearchActivity::class.java)
            startActivity(displayIntent)
        } }
        buttonSearch.setOnClickListener(buttonClickListener)

        val buttonMedia = findViewById<Button>(R.id.button_media)
        buttonMedia.setOnClickListener{
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }

        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener{
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }

    }
}