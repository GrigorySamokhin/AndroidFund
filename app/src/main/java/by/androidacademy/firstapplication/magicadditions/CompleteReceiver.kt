package by.androidacademy.firstapplication.magicadditions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class CompleteReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        Log.d("CompleteReceiver", "#onReceive")
        Toast.makeText(p0, "File downloaded", Toast.LENGTH_SHORT).show()
        val posterPath = p1.getStringExtra(DownloadService.POSTER_PATH) ?: return
        Log.d("CompleteReceiver", "#onReceive, posterPath: $posterPath")
        val trailerIntent = PosterActivity.newIntent(p0, posterPath)
        trailerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        p0.startActivity(trailerIntent)
    }
}