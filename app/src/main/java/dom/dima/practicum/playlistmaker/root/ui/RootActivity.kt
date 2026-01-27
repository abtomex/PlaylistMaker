package dom.dima.practicum.playlistmaker.root.ui

import android.os.Bundle
import android.view.View
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

        _binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.root_fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavView
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioPlayerFragment,
                R.id.agreementFragment -> {
                    hideBottomNavigation()
                }
                else -> {
                    showBottomNavigation()
                }
            }
        }
    }

    private fun showBottomNavigation() {
        binding.bottomNavView.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        binding.bottomNavView.visibility = View.GONE
    }
}