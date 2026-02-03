package dom.dima.practicum.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dom.dima.practicum.playlistmaker.R
import dom.dima.practicum.playlistmaker.databinding.FragmentSettingsBinding
import dom.dima.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val themeSwitcher = binding.themeSwitcher
        val buttonShare = binding.shareText
        val buttonCheckSupport = binding.helpdeskText
        val buttonAgreement = binding.agreementText

        themeSwitcher.isChecked = viewModel.isDarkThemeOn()

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeTheme(checked)
        }
        buttonShare.setOnClickListener {
            viewModel.doShare(getString(R.string.android_course_url))
        }

        buttonCheckSupport.setOnClickListener {
            viewModel.doWrightTechSupport(
                arrayOf(getString(R.string.my_email)),
                getString(R.string.email_subject),
                getString(R.string.email_text)
            )
        }

        buttonAgreement.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_agreementFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}