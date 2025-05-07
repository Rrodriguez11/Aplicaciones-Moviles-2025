package es.uam.eps.dadm.faunary

import android.app.Application
import es.uam.eps.dadm.faunary.data.DataRepository
import timber.log.Timber

/**
 * Clase de aplicación de Faunary.
 * Se ejecuta al iniciar la app, antes que cualquier actividad.
 */
class FaunaryApplication: Application() {

    /**
     * Inicializa Timber para el registro de logs durante el desarrollo.
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // Si ha pasado un día desde la última actualización, aplicamos los cambios
        if (DateUtils.haPasadoUnDia(this)) {
            es.uam.eps.dadm.faunary.data.DataRepository.actualizarEstadoDiario(this)
            Timber.i("Nuevo día detectado: estado actualizado")
        } else {
            Timber.i("Mismo día: no se actualizó el estado")
        }
    }


}