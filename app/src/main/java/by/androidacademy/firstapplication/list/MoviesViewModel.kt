package by.androidacademy.firstapplication.list

import android.content.Context
import androidx.lifecycle.*
import by.androidacademy.firstapplication.R
import by.androidacademy.firstapplication.data.Movie
import by.androidacademy.firstapplication.repository.MoviesRepository
import by.androidacademy.firstapplication.utils.SingleEventLiveData
import by.androidacademy.firstapplication.utils.StringsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class MoviesViewModel(
    private val moviesRepository: MoviesRepository,
    stringsProvider: StringsProvider
) : ViewModel() {

    private val moviesMutableLiveData = SingleEventLiveData<List<Movie>>()
    private val isProgressBarVisibleMutableLiveData = MutableLiveData<Boolean>()
    private val errorMutableLiveData = SingleEventLiveData<String>()

    val movies: LiveData<List<Movie>> = moviesMutableLiveData
    val isProgressBarVisible: LiveData<Boolean> = isProgressBarVisibleMutableLiveData
    val error: LiveData<String> = errorMutableLiveData

    fun loadPopularMovies() {
        viewModelScope.launch {
            try {
                isProgressBarVisibleMutableLiveData.value = true

                val movies = withContext(Dispatchers.IO) { moviesRepository.getPopularMovies() }

                moviesMutableLiveData.value = movies
                isProgressBarVisibleMutableLiveData.value = false
            } catch (error: Throwable) {
                errorMutableLiveData.value = "Error load movies"
            } finally {
                isProgressBarVisibleMutableLiveData.value = false
            }
        }
    }

    fun deleteAllDataFromDatabase() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                moviesRepository.deleteCachedData()
            }
        }
    }
}

class MoviesViewModelFactory(
    private val moviesRepository: MoviesRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass == MoviesViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            MoviesViewModel(
                moviesRepository,
                StringsProvider(context)
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}