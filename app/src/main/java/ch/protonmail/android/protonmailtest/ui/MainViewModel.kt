package ch.protonmail.android.protonmailtest.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.protonmail.android.protonmailtest.data.model.Forecast
import ch.protonmail.android.protonmailtest.data.repository.OfflineFirstForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    forecastRepository: OfflineFirstForecastRepository,
) : ViewModel() {

    private val forecasts: Flow<List<Forecast>> = forecastRepository.getForecasts()
        .catch {
            // TODO: Manage errors here.
            Log.w("PROTON", "Error getting Forecasts")
        }

    val uiState: StateFlow<HottestUiState> = forecasts.map {
        HottestUiState(isLoading = false, forecasts = it)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(), initialValue = HottestUiState()
    )

}


