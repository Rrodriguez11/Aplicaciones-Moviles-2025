package es.uam.eps.dadm.faunary

import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    private const val PREFS_NAME = "faunary_prefs"
    private const val LAST_DATE_KEY = "last_update_date"
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun haPasadoUnDia(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastDateStr = prefs.getString(LAST_DATE_KEY, null)
        val today = LocalDate.now()

        val lastDate = lastDateStr?.let {
            runCatching { LocalDate.parse(it, formatter) }.getOrNull()
        }

        if (lastDate == null || lastDate.isBefore(today)) {
            prefs.edit().putString(LAST_DATE_KEY, today.format(formatter)).apply()
            return true
        }

        return false
    }
}
