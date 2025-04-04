package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.uam.eps.dadm.faunary.R
import timber.log.Timber

/**
 * ViewModel asociado a la pantalla del hábitat.
 * Se encarga de almacenar y gestionar el estado relacionado con la limpieza,
 * alimentación y medicación de los animales.
 * Utiliza LiveData para actualizar automáticamente la interfaz.
 */
class HabitatViewModel(application: Application) : AndroidViewModel(application) {

    // Tipo de comida actual
    val foodType = MutableLiveData<Int>(R.string.meat)

    // Número de animales alimentados y total
    val fedCount = MutableLiveData<Int>(11)
    val totalCount = MutableLiveData<Int>(12)

    // Número de animales medicados y enfermos
    val medicatedCount = MutableLiveData<Int>(3)
    val sickCount = MutableLiveData<Int>(3)

    // Días que faltan para realizar la limpieza (si aún no se ha hecho)
    val cleanDelayDays = MutableLiveData<Int>(2)

    // Frecuencia de limpieza (en días)
    val cleanFrequency = MutableLiveData<Int>(4)

    // Texto que indica cuándo es la próxima limpieza, cambia según el estado
    val cleaningLabel = MutableLiveData<String>()

    // Indica si la limpieza ya ha sido realizada
    private val _cleaningDone = MutableLiveData(false)
    val cleaningDone: LiveData<Boolean> get() = _cleaningDone

    // Evento para mostrar un Toast tras realizar la limpieza
    private val _showCleaningToast = MutableLiveData<Boolean>()
    val showCleaningToast: LiveData<Boolean> get() = _showCleaningToast

    init {
        updateCleaningLabel()
        Timber.i("HabitatViewModel created")
    }

    /**
     * Actualiza el texto `cleaningLabel` según si la limpieza está hecha o no.
     */
    private fun updateCleaningLabel() {
        val done = cleaningDone.value ?: false
        val context = getApplication<Application>()
        val text = if (done) {
            context.getString(R.string.clean_next, cleanFrequency.value)
        } else {
            context.getString(R.string.clean_delay, cleanDelayDays.value)
        }
        cleaningLabel.value = text
    }

    /**
     * Marca la limpieza como realizada, lanza el evento del Toast y actualiza el estado.
     */
    fun markCleaningDone() {
        _cleaningDone.value = true
        _showCleaningToast.value = true
        updateCleaningLabel()
        Timber.i("Habitat was cleaned successfully")
    }

    /**
     * Reinicia el evento del Toast tras mostrarlo una vez.
     */
    fun resetCleaningToast() {
        _showCleaningToast.value = false
    }
}