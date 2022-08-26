package ch.protonmail.android.protonmailtest.data.repository

import android.util.Log
import ch.protonmail.android.protonmailtest.data.local.ForecastDao
import ch.protonmail.android.protonmailtest.data.local.ForecastEntity
import ch.protonmail.android.protonmailtest.data.local.asEntity
import ch.protonmail.android.protonmailtest.data.local.asExternalModel
import ch.protonmail.android.protonmailtest.data.model.Forecast
import ch.protonmail.android.protonmailtest.data.network.ForecastNetworkDataSource
import ch.protonmail.android.protonmailtest.data.sync.Synchronizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstForecastRepository @Inject constructor(
    private val forecastDao: ForecastDao,
    private val network: ForecastNetworkDataSource,
) : ForecastRepository {

    override fun getForecasts(): Flow<List<Forecast>> = forecastDao.getForecasts()
        .map { it.map(ForecastEntity::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        Log.w("PROTON", "This is where the synchronization happens")
        val forecasts = network.getForecasts().map(Forecast::asEntity)
        forecastDao.insertAll(*forecasts.toTypedArray())
        return true
    }

}