package es.uam.eps.dadm.faunary

import android.app.Application
import timber.log.Timber

/**
 * Clase de aplicación de Faunary.
 * Se ejecuta al iniciar la app, antes que cualquier actividad.
 */
class FaunaryApplication: Application() {

    /**
     * Inicializa Timber para el registro de logs durante el desarrollo.
     */
    override fun onCreate(){
        super.onCreate()
        Timber.plant(Timber.DebugTree()) // Habilita logs en Logcat con Timber
    }
}