package dom.dima.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Через анонимный класс
        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            val displayIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(displayIntent)
        } }
        buttonSearch.setOnClickListener(buttonClickListener)

        // Через лямбда
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