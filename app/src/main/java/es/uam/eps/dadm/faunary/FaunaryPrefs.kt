package es.uam.eps.dadm.faunary

import android.content.Context

object FaunaryPrefs {
    private const val PREFS_NAME = "faunary_prefs"

    fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun guardarEstadoLimpieza(context: Context, nombreRecinto: String, limpiezaHecha: Boolean) {
        getPrefs(context)
            .edit()
            .putBoolean("${nombreRecinto}_limpieza", limpiezaHecha)
            .apply()
    }

    fun obtenerEstadoLimpieza(context: Context, nombreRecinto: String): Boolean {
        return getPrefs(context).getBoolean("${nombreRecinto}_limpieza", false)
    }

    fun guardarAnimalAlimentado(context: Context, recinto: String, animal: String, alimentado: Boolean) {
        getPrefs(context)
            .edit()
            .putBoolean("${recinto}_${animal}_alimentado", alimentado)
            .apply()
    }

    fun estaAnimalAlimentado(context: Context, recinto: String, animal: String): Boolean {
        return getPrefs(context).getBoolean("${recinto}_${animal}_alimentado", false)
    }

}
