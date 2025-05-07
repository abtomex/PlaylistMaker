package dom.dima.practicum.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val viewSettings = findViewById<MaterialToolbar>(R.id.settings_layout)
        viewSettings.setNavigationOnClickListener{
            this.finish()
        }
    }
}
