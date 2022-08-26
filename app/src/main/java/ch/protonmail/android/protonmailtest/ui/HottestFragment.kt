package ch.protonmail.android.protonmailtest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ch.protonmail.android.protonmailtest.databinding.FragmentHottestBinding
import ch.protonmail.android.protonmailtest.ui.widget.SpringAddItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Created by ProtonMail on 2/25/19.
 * Shows any days that have less than a 50% chance of rain, ordered hottest to coldest
 * */
@AndroidEntryPoint
class HottestFragment : Fragment() {

    private var _binding: FragmentHottestBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHottestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forecastAdapter = ForecastAdapter { forecast ->
            startActivity(DetailsActivity.newIntent(requireContext(), forecast))
        }
        binding.recyclerView.adapter = forecastAdapter
        binding.recyclerView.itemAnimator = SpringAddItemAnimator()

        fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collect { uiState ->
                    forecastAdapter.submitList(uiState.forecasts
                        .filter { forecast -> forecast.chanceRain >= 0.50f }
                        .sortedBy { forecast -> forecast.chanceRain })
                }
            }
        }
    }

}