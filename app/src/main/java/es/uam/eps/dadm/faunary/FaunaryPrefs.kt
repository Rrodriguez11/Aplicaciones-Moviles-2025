package es.uam.eps.dadm.faunary

import android.content.Context

/**
 * Objeto singleton para gestionar preferencias compartidas de la app.
 * Se encarga de almacenar y recuperar información persistente como estados
 * de limpieza, alimentación y medicación, usando SharedPreferences.
 */
object FaunaryPrefs {
    private const val PREFS_NAME = "faunary_prefs" // Nombre del archivo de preferencias

    // Accede a las preferencias compartidas de la app
    fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** Guarda si la limpieza fue realizada en un recinto específico */
    fun guardarEstadoLimpieza(context: Context, nombreRecinto: String, limpiezaHecha: Boolean) {
        getPrefs(context)
            .edit()
            .putBoolean("${nombreRecinto}_limpieza", limpiezaHecha)
            .apply()
    }

    /** Obtiene si la limpieza fue realizada en un recinto */
    fun obtenerEstadoLimpieza(context: Context, nombreRecinto: String): Boolean {
        return getPrefs(context).getBoolean("${nombreRecinto}_limpieza", false)
    }

    /** Guarda si un animal ha sido alimentado */
    fun guardarAnimalAlimentado(context: Context, recinto: String, animal: String, alimentado: Boolean) {
        getPrefs(context)
            .edit()
            .putBoolean("${recinto}_${animal}_alimentado", alimentado)
            .apply()
    }

    /** Comprueba si un animal está marcado como alimentado */
    fun estaAnimalAlimentado(context: Context, recinto: String, animal: String): Boolean {
        return getPrefs(context).getBoolean("${recinto}_${animal}_alimentado", false)
    }

    /** Guarda que un animal ha sido medicado (no almacena si se desmedica) */
    fun guardarAnimalMedicado(context: Context, recinto: String, animal: String) {
        getPrefs(context)
            .edit()
            .putBoolean("${recinto}_${animal}_medicado", true)
            .apply()
    }

    /** Comprueba si un animal está marcado como medicado */
    fun estaAnimalMedicado(context: Context, recinto: String, animal: String): Boolean {
        return getPrefs(context).getBoolean("${recinto}_${animal}_medicado", false)
    }

    /** Guarda la cantidad de días restantes para limpiar un recinto */
    fun guardarDiasParaLimpieza(context: Context, nombreRecinto: String, dias: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt("limpieza_dias_$nombreRecinto", dias).apply()
    }

    /** Obtiene los días restantes para limpiar un recinto, si están definidos */
    fun obtenerDiasParaLimpieza(context: Context, nombreRecinto: String): Int? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return if (prefs.contains("limpieza_dias_$nombreRecinto")) {
            prefs.getInt("limpieza_dias_$nombreRecinto", 0)
        } else null
    }

    /** Guarda cuántos días faltan para volver a medicar a un animal por enfermedad */
    fun guardarDiasHastaProximaMedicina(context: Context, animalNombre: String, enfermedadNombre: String, dias: Int) {
        val prefs = getPrefs(context)
        prefs.edit().putInt("dias_medicina_${animalNombre}_$enfermedadNombre", dias).apply()
    }

    /** Obtiene cuántos días faltan para volver a medicar a un animal por enfermedad */
    fun obtenerDiasHastaProximaMedicina(context: Context, animalNombre: String, enfermedadNombre: String): Int? {
        val prefs = getPrefs(context)
        val key = "dias_medicina_${animalNombre}_$enfermedadNombre"
        return if (prefs.contains(key)) prefs.getInt(key, 0) else null
    }
}
