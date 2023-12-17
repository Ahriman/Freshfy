package com.marcossan.freshfy.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.marcossan.freshfy.FreshfyApp
import com.marcossan.freshfy.MainActivity
import com.marcossan.freshfy.data.model.Product


// TODO: https://www.youtube.com/watch?v=TEoe4JTQOEA
class NotificationReceiver: BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_ID_EXTRA = "notification_id"
        const val PRODUCT_EXTRA = "product_extra"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context?, intent: Intent?) {
        // Manejar la acción aquí, como mostrar la notificación
        if (context != null && intent != null) {
            val notificationId = intent.getIntExtra(NOTIFICATION_ID_EXTRA, 0)

//            val product = intent.getParcelableExtra<Product>(
//                PRODUCT_EXTRA,
//                Product::class.java.classLoader
//            )

//            val product = intent.getParcelableExtra<Product>(PRODUCT_EXTRA)
//                ?: intent.getSerializableExtra(PRODUCT_EXTRA) as? Product


            // Mostrar la notificación usando el ID
//            if (product != null) {
//                createSimpleNotification(context, notificationId, product)
//            }
        }
//        createSimpleNotification(context)
    }

    private fun createSimpleNotification(context: Context, notificationId: Int, product: Product) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notification = NotificationCompat.Builder(context, FreshfyApp.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_delete)
            .setContentTitle("My title")
            .setContentText("Esto es un ejemplo <3")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

}