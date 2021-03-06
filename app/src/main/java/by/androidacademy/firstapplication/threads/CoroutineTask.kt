package by.androidacademy.firstapplication.threads

import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

private const val LOG_TAG = "CoroutineTask"

class CoroutineTask(): CoroutineScope {

    private var newJob: Job? = null

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob()


    fun createTask() {
        newJob = launch(context = Dispatchers.IO, start = CoroutineStart.LAZY) {
            Log.d(LOG_TAG, "Start job on IO thread | thread ${Thread.currentThread().name}")
            repeat(10) { counter ->
                Log.d(LOG_TAG, "New counter value [counter: $counter] | thread: ${Thread.currentThread().name}")

                launch(Dispatchers.Main) {
                    Log.d(LOG_TAG, "Switch thread to main [counter: $counter] | thread: ${Thread.currentThread().name}")
               // listener.onProgressUpdate(counter)
                }
                delay(500)
            }
            launch(Dispatchers.Main) {
                Log.d(LOG_TAG, "Switch thread to main again | thread: ${Thread.currentThread().name}")
                //listener.onPostExecute()
            }
        }
       // listener.onPreExecute()
    }
    fun cancel() {
        Log.d(LOG_TAG, "Before 'cancel' of job | thread: ${Thread.currentThread().name}")

        newJob?.cancel()
        coroutineContext.cancel()

        Log.d(LOG_TAG, "Before 'cancel' of job | thread: ${Thread.currentThread().name}")
    }

    fun start(): Boolean? {
        Log.d(LOG_TAG, "Before 'start' of job | thread: ${Thread.currentThread().name}")
        val started = newJob?.start()
        Log.d(LOG_TAG, "After 'start' of job (started: $started) | thread: ${Thread.currentThread().name}")

        return started
    }

}