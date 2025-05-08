package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory personalizada para crear instancias de HabitatViewModel
 * con parámetros personalizados (application y habitatName).
 */
class HabitatViewModelFactory(
    private val application: Application,
    private val habitatName: String
) : ViewModelProvider.Factory {

    // Método requerido para instanciar el ViewModel con los parámetros personalizados
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica que el ViewModel solicitado sea del tipo esperado
        if (modelClass.isAssignableFrom(HabitatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitatViewModel(application, habitatName) as T
        }
        // Si se solicita un ViewModel diferente, lanza una excepción
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
