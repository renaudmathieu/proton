# Version 1. Singe module and Fragments

If I had had the opportunity to start the project from scratch, I would not have gone with this architecture (a single module and use of Fragments). 
However, the instructions mention **correcting problems** and **working in a team** (I don't know if the team is up for Compose right now so it's not my decision at the moment).

I would have liked to take more time to show how to structure a **multi-module application** and use **Jetpack Compose**.

I've already done many things to prepare the project for a multi-module architecture:
- Offline synchronization management, 
- ViewModels
- Dagger Hilt. 
- Clean separation of the data layer.

I will be happy to show projects and discuss them but for now, let's review the layers:

## Data layer

The project retrieves data with a **Offline first strategy** in mind based in [this very recent article](https://developer.android.com/topic/architecture/data-layer/offline-first) from Google.

By using **WorkManager** and **AppStartup**, everything is automatically fetched when running the app.

Workmanager is great for this thanks to :

```kotlin
val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
```

Let's have a look at both interfaces I've created:

```kotlin
/**
 * Interface marker for a class that manages synchronization between local data and a remote
 * source for a [Syncable].
 */
interface Synchronizer {

    /**
     * Syntactic sugar to call [Syncable.syncWith] while omitting the synchronizer argument
     */
    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)
}
```
```kotlin
/**
 * Interface marker for a class that is synchronized with a remote source. Syncing must not be
 * performed concurrently and it is the [Synchronizer]'s responsibility to ensure this.
 */
interface Syncable {

    /**
     * Synchronizes the local database backing the repository with the network.
     * Returns if the sync was successful or not.
     */
    suspend fun syncWith(synchronizer: Synchronizer): Boolean
}
```

My dedicated `SyncWorker` extends `Synchronizer` interface to "trigger" synchronization to repositories. 

Note that with Dagger Hilt, injecting Repositories is quite easy.

```kotlin
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val forecastRepository: ForecastRepository,
) : CoroutineWorker(appContext, workerParams), Synchronizer
```

On the data-side, any Repository should implement `Syncable` and I can apply any strategy. I've used **Room** to store a list of `ForecastEntity`.

```kotlin
    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        Log.w("PROTON", "This is where the synchronization happens")
        val forecasts = network.getForecasts().map(Forecast::asEntity)
        forecastDao.insertAll(*forecasts.toTypedArray())
        return true
    }
```

## Domain layer

There is only one model in the project. **Domain should not rely on Android Framework** but I took a small shortcut here: I didn't map `Forecast` into `ForecastUiModel` in the UI layer.

Maybe I will do it during the weekend.

Therefore, `Forecast` is using `@Parcelize` **annotations and should not**.

NB: The project was using deprecated `kotlin-extensions` and is now using only `parcelize`.

## UI Layer

This is where everything could be migrated in Jetpack Compose.

Almost everything about this layer comes from [this link](https://developer.android.com/topic/architecture/ui-layer).

I also migrated the project to Material3.

Since my ViewModel is shared via Activity, I have everything ready to filter hottest days : 

```kotlin
forecastAdapter.submitList(uiState.forecasts
    .filter { forecast -> forecast.chanceRain >= 0.50f }
    .sortedBy { forecast -> forecast.chanceRain })
```

Side-note: I've added `kotlinx-datetime` dependency and I'm quite happy about it. To show sunset or sunrise:

```kotlin
LocalTime.fromSecondOfDay(forecast.sunrise.toInt())
```


## What is missing?

### Tests! 
I always need to write tests especially for the database part but as there is one table with no relationship, I've considered it a minor priority
I ran manual test about synchronization.

### Image

I didn't really understand what the story behind downloading images in a Weather app. 

Since I was not sure, I used both Picasso (because minSDK is 15) and the `DownloadManager` with a Broadcast receiver. I hope this is what you were expecting.


