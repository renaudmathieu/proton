package ch.protonmail.android.protonmailtest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network representation of [Forecast]
 */
@Serializable
@Parcelize
data class Forecast(
    val day: String,
    val description: String,
    val sunrise: Long,
    val sunset: Long,
    @SerialName("chance_rain") val chanceRain: Float,
    val high: Int,
    val low: Int,
    val image: String,
) : Parcelable