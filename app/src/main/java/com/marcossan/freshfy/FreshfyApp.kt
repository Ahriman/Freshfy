package com.marcossan.freshfy

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FreshfyApp : Application() {
    companion object {
        const val CHANNEL_ID = "freshfy_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificacionChannel()
    }

    private fun createNotificacionChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // TODO: Obsoleto porque la app ya utiliza mínimo la versión 26
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_product_expire_date),
                NotificationManager.IMPORTANCE_HIGH,
            )

            // TODO: Cambiar el nombre a uno más descritivo, esto sale luego en info de la app
            channel.description = "Este canal se utiliza para notificar cuándo un producto va a caducar. Avisará con suficiente tiempo de antelación." // TODO+ Character.toChars(0x1F60A) https://stackoverflow.com/questions/26893796/how-to-set-emoji-by-unicode-in-a-textview
            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
    }
}