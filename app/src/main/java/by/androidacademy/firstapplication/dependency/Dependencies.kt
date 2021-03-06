package by.androidacademy.firstapplication.dependency

import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import androidx.room.Room
import by.androidacademy.firstapplication.App
import by.androidacademy.firstapplication.androidservices.HeavyWorkerManager
import by.androidacademy.firstapplication.androidservices.ServiceDelegate
import by.androidacademy.firstapplication.androidservices.WorkerParamsRequest
import by.androidacademy.firstapplication.androidservices.viewmodel.ServiceViewModelState
import by.androidacademy.firstapplication.api.TmdbServiceApi
import by.androidacademy.firstapplication.db.AppDatabase
import by.androidacademy.firstapplication.repository.MoviesRepository
import by.androidacademy.firstapplication.repository.TmdbServiceMapper
import by.androidacademy.firstapplication.repository.cache.MoviesCacheImpl
import by.androidacademy.firstapplication.repository.cache.SharedPreferencesImpl
import by.androidacademy.utils.NotificationsManager
import by.androidacademy.utils.ResourceManager
import kotlinx.android.synthetic.main.fragment_threads.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Dependencies {

    private val db by lazy {
        createRoomDatabase()
    }

    val moviesRepository by lazy {
        createMoviesRepository()
    }

    private fun createMoviesRepository(): MoviesRepository {
        return MoviesRepository(
            createTmdbServiceApi(),
            createTmdbServiceMapper(),
            createMoviesCache()
        )
    }

    private fun createMoviesCache(): MoviesCacheImpl {
        return MoviesCacheImpl(
            db.movieDao(),
            db.videoDao(),
            SharedPreferencesImpl(App.instance)
        )
    }

    private fun createRoomDatabase(): AppDatabase {
        return Room.databaseBuilder(
            App.instance,
            AppDatabase::class.java,
            "movies.db"
        ).build()
    }

    private fun createTmdbServiceApi(): TmdbServiceApi {
        return createRetrofit().create()
    }

    private fun createTmdbServiceMapper(): TmdbServiceMapper {
        return TmdbServiceMapper()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    val heavyWorkManager by lazy {
        createHeavyWorkManager()
    }

    val serviceDelegate by lazy {
        createServiceDelegate()
    }

    val notificationsManager by lazy {
        createNotificationsManager()
    }

    val workerParamsRequest by lazy {
        createWorkerParamsRequest()
    }

    fun serviceViewModelState() = ServiceViewModelState()

    private fun createWorkerParamsRequest(): WorkerParamsRequest {
        return WorkerParamsRequest()
    }

    private fun createNotificationsManager(): NotificationsManager {
        return NotificationsManager(
            App.instance,
            ResourceManager(App.instance),
            App.instance.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        )
    }

    private fun createServiceDelegate(): ServiceDelegate = ServiceDelegate()

    private fun createHeavyWorkManager(): HeavyWorkerManager = HeavyWorkerManager()


}