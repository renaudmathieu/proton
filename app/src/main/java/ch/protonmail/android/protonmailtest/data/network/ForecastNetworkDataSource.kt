package ch.protonmail.android.protonmailtest.data.network

import ch.protonmail.android.protonmailtest.data.model.Forecast
import javax.inject.Inject

class ForecastNetworkDataSource @Inject constructor(
    private val forecastApi: ForecastApi
) {

    suspend fun getForecasts(): List<Forecast> = forecastApi.getForecasts()

}