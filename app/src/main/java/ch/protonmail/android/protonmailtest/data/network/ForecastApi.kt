package ch.protonmail.android.protonmailtest.data.network

import ch.protonmail.android.protonmailtest.data.model.Forecast
import retrofit2.http.GET

interface ForecastApi {

    @GET("forecast")
    suspend fun getForecasts(): List<Forecast>

}