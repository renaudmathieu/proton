package ch.protonmail.android.protonmailtest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ch.protonmail.android.protonmailtest.data.model.Forecast

@Entity(
    tableName = "forecasts",
)
data class ForecastEntity(
    @PrimaryKey val day: String,
    val description: String,
    val sunrise: Long,
    val sunset: Long,
    val chanceRain: Float,
    val high: Int,
    val low: Int,
    val image: String,
)

fun ForecastEntity.asExternalModel() = Forecast(
    day = day,
    description = description,
    sunrise = sunrise,
    sunset = sunset,
    chanceRain = chanceRain,
    high = high,
    low = low,
    image = image,
)

fun Forecast.asEntity() = ForecastEntity(
    day = day,
    description = description,
    sunrise = sunrise,
    sunset = sunset,
    chanceRain = chanceRain,
    high = high,
    low = low,
    image = image,
)