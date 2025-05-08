package es.uam.eps.dadm.faunary

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Objeto utilitario para verificar si ha pasado un día desde la última actualización.
 * Utiliza SharedPreferences para guardar la última fecha y compararla con la actual.
 */
object DateUtils {

    // Nombre del archivo de preferencias
    private const val PREFS_NAME = "faunary_prefs"
    // Clave usada para almacenar la última fecha de actualización
    private const val LAST_DATE_KEY = "last_update_date"
    // Formato estándar de fecha (ISO: yyyy-MM-dd)
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    /**
     * Verifica si ha pasado al menos un día desde la última ejecución registrada.
     * Si es así, actualiza la fecha en SharedPreferences y devuelve true.
     */
    fun haPasadoUnDia(context: Context): Boolean {
        // Obtiene las preferencias compartidas
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Recupera la fecha guardada como String
        val lastDateStr = prefs.getString(LAST_DATE_KEY, null)

        // Fecha actual
        val today = LocalDate.now()

        // Intenta parsear la fecha anterior (puede ser null o inválida)
        val lastDate = lastDateStr?.let {
            runCatching { LocalDate.parse(it, formatter) }.getOrNull()
        }

        // Si no hay fecha previa o es anterior a hoy, actualizamos y devolvemos true
        if (lastDate == null || lastDate.isBefore(today)) {
            prefs.edit().putString(LAST_DATE_KEY, today.format(formatter)).apply()
            return true
        }

        // Si la fecha es de hoy o posterior, no ha pasado un día
        return false
    }
}
