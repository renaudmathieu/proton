package ch.protonmail.android.protonmailtest.data.local

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesForecastDao(
        database: ForecastDatabase,
    ): ForecastDao = database.forecastDao()

}
