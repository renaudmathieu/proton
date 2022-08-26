package ch.protonmail.android.protonmailtest.data.network

import ch.protonmail.android.protonmailtest.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

        val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
            .addInterceptor(httpLoggingInterceptor)

        return builder.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        client: OkHttpClient,
        networkJson: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://5c5c8ba58d018a0014aa1b24.mockapi.io/api/")
            .client(client)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun providesForecastApi(retrofit: Retrofit): ForecastApi = retrofit.create(ForecastApi::class.java)
}