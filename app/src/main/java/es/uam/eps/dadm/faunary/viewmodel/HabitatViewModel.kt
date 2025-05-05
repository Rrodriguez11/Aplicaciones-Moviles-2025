package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.uam.eps.dadm.faunary.model.Animal
import es.uam.eps.dadm.faunary.model.Enfermedad
import es.uam.eps.dadm.faunary.model.Recinto
import timber.log.Timber

/**
 * ViewModel asociado a la pantalla del hábitat.
 * Se encarga de almacenar y gestionar el estado relacionado con la limpieza,
 * alimentación y medicación de los animales.
 * Utiliza LiveData para actualizar automáticamente la interfaz.
 */
class HabitatViewModel(application: Application) : AndroidViewModel(application) {

    // Creamos un recinto de ejemplo con algunos animales
    private val _recinto = MutableLiveData<Recinto>().apply {
        value = Recinto(
            nombre = "Sabana",
            tipo = "Terrestre - Carnívoros",
            capacidad = 5,
            diasEntreLimpiezas = 3,
            limpiezaHecha = false,
            animales = mutableListOf(
                Animal(
                    nombre = "Simba",
                    especie = "León",
                    fechaNacimiento = "2019-05-12",
                    peso = 180.0,
                    hambre = true,
                    enfermedades = mutableListOf(
                        Enfermedad("Pulgas", "2024-05-01", superada = false, medicinaDada = false)
                    )
                ),
                Animal(
                    nombre = "Nala",
                    especie = "Leona",
                    fechaNacimiento = "2020-08-03",
                    peso = 150.0,
                    hambre = false
                )
            )
        )
    }

    val recinto: LiveData<Recinto> get() = _recinto

    private val _fedCount = MutableLiveData(
        _recinto.value?.animales?.count { !it.hambre } ?: 0
    )
    val fedCount: LiveData<Int> get() = _fedCount

    val totalCount: LiveData<Int> = MutableLiveData(
        _recinto.value?.animales?.size ?: 0
    )


    val sickCount: LiveData<Int> = MutableLiveData(
        _recinto.value?.animales?.count { it.estaEnfermo() } ?: 0
    )
    val medicatedCount: LiveData<Int> = MutableLiveData(
        _recinto.value?.animales?.count { it.estaEnfermo() && !it.necesitaMedicina() } ?: 0
    )

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
            context.getString(es.uam.eps.dadm.faunary.R.string.clean_next, cleanFrequency.value)
        } else {
            context.getString(es.uam.eps.dadm.faunary.R.string.clean_delay, cleanDelayDays.value)
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

    fun alimentarAnimal(animal: Animal) {
        animal.hambre = false
        _fedCount.value = _recinto.value?.animales?.count { !it.hambre }
    }
}