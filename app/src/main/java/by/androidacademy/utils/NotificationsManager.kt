package by.androidacademy.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat.*
import by.androidacademy.firstapplication.App
import by.androidacademy.firstapplication.R

private const val SERVICE_NOTIFICATION_ID = 103
private const val DOWNLOAD_GROUP = "download_group"


class NotificationsManager(
    context: App,
    private val resourceManager: ResourceManager,
    private val notificationManager: NotificationManager
) {

    init { createChannel() }

    val notificationBuilder: Builder by lazy {
        createNotification(context)
    }
    private val channelId: String by lazy { resourceManager.packageName }
    private val channelName: String by lazy { resourceManager.getString(R.string.notification_channel_name) }
    private val iconNotification: Bitmap? by lazy { resourceManager.getBitmap(android.R.mipmap.sym_def_app_icon) }
    private val indicatorColor: Int by lazy { resourceManager.getColor(android.R.color.holo_green_light) }

    fun showNotification(): Notification {
        return with(
            notificationBuilder
                .setContentTitle(channelName)
                .build()
        )
        { showNotification(this).run { this@with } }
    }

    fun hideNotification() {
        notificationManager.cancel(SERVICE_NOTIFICATION_ID)
    }

    private fun showNotification(notification: Notification) {
        notificationManager.notify(SERVICE_NOTIFICATION_ID, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(
                channelId
            ) == null
        ) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    IMPORTANCE_DEFAULT
                ).apply {
                    description = channelName
                    setSound(null, null)
                    setShowBadge(false)
                })
        }
    }


    private fun createNotification(context: Context): Builder {
        return Builder(context, channelId)
            .setCategory(CATEGORY_EVENT)
            .setPriority(PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentText(resourceManager.getString(R.string.notification_text))
            .setLargeIcon(iconNotification)
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
            .setColor(indicatorColor)
            .setGroup(DOWNLOAD_GROUP)
            .setSound(null)
            .setLights(indicatorColor, 1000, 1000)
            .setVisibility(VISIBILITY_PUBLIC)
    }

}