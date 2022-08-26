package ch.protonmail.android.protonmailtest.data.repository

import ch.protonmail.android.protonmailtest.data.sync.Syncable
import ch.protonmail.android.protonmailtest.data.model.Forecast
import kotlinx.coroutines.flow.Flow

interface ForecastRepository: Syncable {

    fun getForecasts(): Flow<List<Forecast>>

}