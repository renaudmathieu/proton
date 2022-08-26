package ch.protonmail.android.protonmailtest.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ch.protonmail.android.protonmailtest.R
import ch.protonmail.android.protonmailtest.data.model.Forecast
import ch.protonmail.android.protonmailtest.databinding.ItemForecastBinding
import ch.protonmail.android.protonmailtest.ui.ForecastAdapter.DayViewHolder
import com.squareup.picasso.Picasso

/**
 * Created by ProtonMail on 2/25/19.
 */
class ForecastAdapter(
    private val listener: (Forecast) -> Unit
) : ListAdapter<Forecast, DayViewHolder>(POST_COMPARATOR) {

    companion object {

        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Forecast>() {

            override fun areContentsTheSame(
                oldItem: Forecast,
                newItem: Forecast
            ): Boolean = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: Forecast,
                newItem: Forecast
            ): Boolean = oldItem.day == newItem.day
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val itemBinding = ItemForecastBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        getItem(position)?.let { forecast ->
            holder.bindHolder(forecast, listener)
        }
    }

    class DayViewHolder(val itemBinding: ItemForecastBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindHolder(forecast: Forecast, listener: (Forecast) -> Unit) {
            with(itemView) {
                itemBinding.title.text = context.getString(
                    R.string.item_forecast_title,
                    forecast.day,
                    forecast.description
                )
                Picasso.get().load(forecast.image).into(itemBinding.image)
                itemBinding.container.setOnClickListener {
                    listener(forecast)
                }
            }
        }
    }

}