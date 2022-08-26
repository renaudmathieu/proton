package ch.protonmail.android.protonmailtest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Query("SELECT * FROM forecasts")
    fun getForecasts(): Flow<List<ForecastEntity>>

    /**
     * TODO: Consider upsert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg forecasts: ForecastEntity)

}