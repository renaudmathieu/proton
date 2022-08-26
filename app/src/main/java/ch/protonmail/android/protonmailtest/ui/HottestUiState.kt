package ch.protonmail.android.protonmailtest.ui

import ch.protonmail.android.protonmailtest.data.model.Forecast

data class HottestUiState(
    val isLoading: Boolean = true,
    val forecasts: List<Forecast> = listOf()
)
