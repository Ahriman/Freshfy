package com.marcossan.freshfy.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class Utils {

    // Métodos estáticos
    companion object {
        fun getTimeMillisOfStringDate(fechaString: String): Long {
            try {
                // Define el formato de la cadena de fecha
                val formato = SimpleDateFormat("dd/MM/yyyy")

                // Parsea la cadena de fecha y obtiene el objeto Date
                val fecha = formato.parse(fechaString)

                // Obtiene los milisegundos de la fecha
                return fecha?.time ?: -1
            } catch (e: Exception) {
                // Maneja cualquier excepción que pueda ocurrir durante el proceso de parsing
                e.printStackTrace()
                return -1
            }
        }

        fun getStringDateFromMillis(millis: Long): String {
            // Define the desired date format
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")

            // Create a Date object using the provided timestamp in milliseconds
            val date = Date(millis)

            // Format the Date object to a string using the specified format
            return dateFormat.format(date)
        }

        fun calculateDaysUntilExpiration(expirationDateInMillis: Long): Long {
            val currentTimeInMillis = System.currentTimeMillis()
            val timeDifferenceInMillis = expirationDateInMillis - currentTimeInMillis

            // Convertimos la diferencia de tiempo a días redondeando hacia abajo
            return TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis)
        }

    }
}