package ch.protonmail.android.protonmailtest

import android.app.Application
import ch.protonmail.android.protonmailtest.data.sync.Sync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Sync.initialize(context = this)
    }
}