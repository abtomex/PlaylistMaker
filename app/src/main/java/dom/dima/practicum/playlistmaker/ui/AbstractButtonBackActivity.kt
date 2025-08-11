package dom.dima.practicum.playlistmaker.ui

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

abstract class AbstractButtonBackActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        implementButtonBack()

    }

    fun implementButtonBack() {
        val buttonBack = findViewById<MaterialToolbar>(buttonBackId())
        buttonBack.setNavigationOnClickListener{
            this.finish()
        }
    }

    abstract fun buttonBackId(): Int

}