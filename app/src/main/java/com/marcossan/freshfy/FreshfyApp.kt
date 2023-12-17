package com.marcossan.freshfy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FreshfyApp : Application() {
    companion object {
        const val CHANNEL_ID = "freshfy_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
//        createNotificacionChannel()
//        scheduleNotification()
    }

//    @SuppressLint("ScheduleExactAlarm")
//    private fun scheduleNotification() {
//        val intent = Intent(applicationContext, NotificationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            applicationContext,
//            NOTIFICATION_ID,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        // Programar la acción del BroadcastReceiver para ejecutarse en el futuro
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis + 5000, pendingIntent)
//    }

//    private fun createNotificacionChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // TODO: Obsoleto porque la app ya utiliza mínimo la versión 26
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                getString(R.string.notification_product_expire_date),
////                NotificationManager.IMPORTANCE_HIGH,
//                NotificationManager.IMPORTANCE_DEFAULT,
//            ).apply {
////                description = ""
//            }
//
//            // TODO: Cambiar el nombre a uno más descritivo, esto sale luego en info de la app
//            channel.description = "Este canal se utiliza para notificar cuándo un producto va a caducar. Avisará con suficiente tiempo de antelación." // TODO+ Character.toChars(0x1F60A) https://stackoverflow.com/questions/26893796/how-to-set-emoji-by-unicode-in-a-textview
//            val notificationManager = this.getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//
//        }
//    }
}