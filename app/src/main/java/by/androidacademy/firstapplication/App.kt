package by.androidacademy.firstapplication

import android.app.Application
import okhttp3.internal.Internal.instance

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}