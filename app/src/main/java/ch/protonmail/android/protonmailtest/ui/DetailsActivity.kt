package ch.protonmail.android.protonmailtest.ui

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ch.protonmail.android.protonmailtest.R
import ch.protonmail.android.protonmailtest.data.model.Forecast
import ch.protonmail.android.protonmailtest.databinding.ActivityDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.datetime.LocalTime
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * Created by ProtonMail on 2/25/19.
 * Shows all the details for a particular day.
 */
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private var forecast: Forecast? = null
    private var downloadId: Long? = null

    private val downloadManagerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                Toast.makeText(this@DetailsActivity, "Download finished", Toast.LENGTH_SHORT).show()
                binding.image.isVisible = true
                binding.download.isVisible = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        intent?.extras?.let {
            forecast = it.getParcelable(EXTRA_FORECAST) // API LEVEL 33 :)
        }

        initScreen()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(
            downloadManagerReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    override fun onStop() {
        unregisterReceiver(downloadManagerReceiver)
        super.onStop()
    }

    private fun initScreen() = with(binding) {
        forecast?.let { forecast ->

            topAppBar.title = getString(R.string.details_top_bar_title, forecast.day)
            topAppBar.setNavigationOnClickListener { finish() }

            description.text = forecast.description

            sunrise.text = getString(
                R.string.details_label_sunrise,
                LocalTime.fromSecondOfDay(forecast.sunrise.toInt())
            )

            sunset.text = getString(
                R.string.details_label_sunset,
                LocalTime.fromSecondOfDay(forecast.sunset.toInt())
            )

            val decimalFormat = DecimalFormat("#.##").apply { roundingMode = RoundingMode.CEILING }
            chanceRain.text = "${decimalFormat.format(forecast.chanceRain * 100)}%"

            high.text = getString(R.string.details_label_high, forecast.high)
            low.text = getString(R.string.details_label_low, forecast.low)

            Picasso.get().load(forecast.image).into(image)

            download.setOnClickListener {
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val request = DownloadManager.Request(Uri.parse(forecast.image))
                    .setTitle(forecast.description)
                    .setDescription("Downloading")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(
                        Uri.fromFile(File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), ""))
                    )
                downloadId = downloadManager.enqueue(request)
            }
        }
    }

    companion object {

        private const val EXTRA_FORECAST: String = "extra:forecast"

        fun newIntent(context: Context, forecast: Forecast): Intent =
            Intent(context, DetailsActivity::class.java).putExtra(EXTRA_FORECAST, forecast)

    }
}
