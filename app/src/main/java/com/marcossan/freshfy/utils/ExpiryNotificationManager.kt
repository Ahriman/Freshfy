package com.marcossan.freshfy.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.marcossan.freshfy.R
import java.util.Calendar
import java.util.Date

class ExpiryNotificationManager(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "expiry_channel"
        private const val NOTIFICATION_ID = 1
        private const val DAYS_BEFORE_EXPIRY = 5
    }

    fun scheduleExpiryNotification(expiryDate: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = expiryDate
        calendar.add(Calendar.DAY_OF_YEAR, -DAYS_BEFORE_EXPIRY)

        if (calendar.timeInMillis > System.currentTimeMillis()) {
            createNotificationChannel()

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_notificacion)
                .setContentTitle("¡Atención!")
                .setContentText("Uno de tus productos caducará en $DAYS_BEFORE_EXPIRY días.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
//                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Expiry Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones para productos próximos a caducar"
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}