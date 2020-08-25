package by.androidacademy.firstapplication.threads

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.androidacademy.firstapplication.R

class CoroutinesViewModel() : ViewModel() {

    private val textMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private var coroutineTask: CoroutineTask? = null

    val text: LiveData<String> = textMutableLiveData

    fun onCreateTask() {
        textMutableLiveData.value = "On create"
        Log.i("View model", "onCreateTask")
        coroutineTask = CoroutineTask()
            .apply { createTask() }
    }

    fun onStartTask() {
        val started = coroutineTask?.start()
        Log.i("View model", "onStartTask")
        if (started == null || started == false) {
            textMutableLiveData.value = "Should create"
        }
    }

    fun onCancelTask() {
        val canceled = coroutineTask?.cancel()

        if (canceled == null) {
            textMutableLiveData.value = "Should create"
        }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineTask?.cancel()
    }
}