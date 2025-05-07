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

    fun guardarAnimalMedicado(context: Context, recinto: String, animal: String) {
        getPrefs(context)
            .edit()
            .putBoolean("${recinto}_${animal}_medicado", true)
            .apply()
    }

    fun estaAnimalMedicado(context: Context, recinto: String, animal: String): Boolean {
        return getPrefs(context).getBoolean("${recinto}_${animal}_medicado", false)
    }

    fun guardarDiasParaLimpieza(context: Context, nombreRecinto: String, dias: Int) {
        val prefs = context.getSharedPreferences("faunary_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("limpieza_dias_$nombreRecinto", dias).apply()
    }

    fun obtenerDiasParaLimpieza(context: Context, nombreRecinto: String): Int? {
        val prefs = context.getSharedPreferences("faunary_prefs", Context.MODE_PRIVATE)
        return if (prefs.contains("limpieza_dias_$nombreRecinto")) {
            prefs.getInt("limpieza_dias_$nombreRecinto", 0)
        } else null
    }


    fun guardarDiasHastaProximaMedicina(context: Context, animalNombre: String, enfermedadNombre: String, dias: Int) {
        val prefs = getPrefs(context)
        prefs.edit().putInt("dias_medicina_${animalNombre}_$enfermedadNombre", dias).apply()
    }

    fun obtenerDiasHastaProximaMedicina(context: Context, animalNombre: String, enfermedadNombre: String): Int? {
        val prefs = getPrefs(context)
        val key = "dias_medicina_${animalNombre}_$enfermedadNombre"
        return if (prefs.contains(key)) prefs.getInt(key, 0) else null
    }




}
