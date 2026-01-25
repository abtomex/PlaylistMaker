package dom.dima.practicum.playlistmaker.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.ActivityRootBinding
import androidx.navigation.ui.setupWithNavController

class RootActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityRootBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Привязываем вёрстку к экрану
        _binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.root_fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavView
        bottomNavigationView.setupWithNavController(navController)
    }
}