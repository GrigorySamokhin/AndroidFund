package by.androidacademy.firstapplication.details

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

class DetailsViewModel(
    private val moviesRepository: MoviesRepository,
    private val stringsProvider: StringsProvider,
    private val movie: Movie
) : ViewModel() {

    private val trailerUrlMutableLiveData = SingleEventLiveData<String>()
    private val errorMutableLiveData = SingleEventLiveData<String>()

    val openTrailerUrl: LiveData<String> = trailerUrlMutableLiveData
    val error: LiveData<String> = errorMutableLiveData

    fun onTrailerButtonClicked() {
        viewModelScope.launch {
            try {
                trailerUrlMutableLiveData.value = withContext(Dispatchers.IO) {
                    moviesRepository.getMovieTrailerUrl(movie)
                }
            } catch (error: Throwable) {
                errorMutableLiveData.value = stringsProvider.getString(
                    R.string.error_load_trailer,
                    error.message ?: ""
                )
            }
        }
    }
}

class DetailsViewModelFactory(
    private val moviesRepository: MoviesRepository,
    private val context: Context,
    private val movie: Movie
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass == DetailsViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            DetailsViewModel(
                moviesRepository,
                StringsProvider(context),
                movie
            ) as T
        } else {
            throw IllegalArgumentException()
        }
    }

}