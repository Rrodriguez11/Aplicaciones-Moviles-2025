package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.uam.eps.dadm.faunary.FaunaryPrefs
import es.uam.eps.dadm.faunary.model.Animal
import es.uam.eps.dadm.faunary.model.Enfermedad
import es.uam.eps.dadm.faunary.model.Recinto
import timber.log.Timber
import es.uam.eps.dadm.faunary.data.DataRepository



/**
 * ViewModel asociado a la pantalla del hábitat.
 * Se encarga de almacenar y gestionar el estado relacionado con la limpieza,
 * alimentación y medicación de los animales.
 * Utiliza LiveData para actualizar automáticamente la interfaz.
 */
class HabitatViewModel(application: Application, habitatName: String) : AndroidViewModel(application) {

    // Creamos un recinto de ejemplo con algunos animales
    private val _recinto = MutableLiveData<Recinto>().apply {
        value = DataRepository.getRecintoByNombre(habitatName)
        requireNotNull(value) { "Recinto no encontrado: $habitatName" }
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
    private val _cleaningLabel = MutableLiveData<String>()
    val cleaningLabel: LiveData<String> get() = _cleaningLabel


    // Indica si la limpieza ya ha sido realizada
    private val _cleaningDone = MutableLiveData(false)
    val cleaningDone: LiveData<Boolean> get() = _cleaningDone

    // Evento para mostrar un Toast tras realizar la limpieza
    private val _showCleaningToast = MutableLiveData<Boolean>()
    val showCleaningToast: LiveData<Boolean> get() = _showCleaningToast

    init {
        // Cargar el recinto desde el repositorio
        val recintoCargado = DataRepository.getRecintoByNombre(habitatName)
        requireNotNull(recintoCargado) { "Recinto no encontrado: $habitatName" }

        // Restaurar si ya había sido limpiado
        recintoCargado.limpiezaHecha = FaunaryPrefs.obtenerEstadoLimpieza(getApplication(), recintoCargado.nombre)

        FaunaryPrefs.obtenerDiasParaLimpieza(getApplication(), recintoCargado.nombre)?.let {
            recintoCargado.diasEntreLimpiezas = it
        }

        // Asignar al LiveData
        _recinto.value = recintoCargado

        // Actualizar LiveData de limpieza
        _cleaningDone.value = recintoCargado.limpiezaHecha
        actualizarCleaningLabel()


        Timber.i("HabitatViewModel creado con limpieza restaurada: ${recintoCargado.limpiezaHecha}")

        recintoCargado.animales.forEach { animal ->
            val alimentado = FaunaryPrefs.estaAnimalAlimentado(getApplication(), recintoCargado.nombre, animal.nombre)
            animal.hambre = !alimentado

            if (FaunaryPrefs.estaAnimalMedicado(getApplication(), recintoCargado.nombre, animal.nombre)) {
                animal.enfermedades.forEach { it.medicinaDada = true }
            }
        }

    }


    /**
     * Marca la limpieza como realizada, lanza el evento del Toast y actualiza el estado.
     */
    fun markCleaningDone() {
        _cleaningDone.value = true

        _recinto.value?.let { recinto ->
            recinto.limpiezaHecha = true
            recinto.diasEntreLimpiezas = recinto.diasEntreLimpiezasOriginal
            FaunaryPrefs.guardarEstadoLimpieza(getApplication(), recinto.nombre, true)
        }

        actualizarCleaningLabel()

        _showCleaningToast.value = true
        Timber.i("Habitat was cleaned successfully (estado guardado)")
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

        // Guardar estado de alimentación
        _recinto.value?.let {
            FaunaryPrefs.guardarAnimalAlimentado(getApplication(), it.nombre, animal.nombre, true)
        }
    }


    fun medicarAnimal(animal: Animal) {
        animal.medicar()
        _recinto.value = _recinto.value

        _recinto.value?.let {
            FaunaryPrefs.guardarAnimalMedicado(getApplication(), it.nombre, animal.nombre)
        }

        (medicatedCount as MutableLiveData).value =
            _recinto.value?.animales?.count { it.estaEnfermo() && !it.necesitaMedicina() } ?: 0
    }


    private val _actualizarUI = MutableLiveData<Boolean>()
    val actualizarUI: LiveData<Boolean> get() = _actualizarUI

    fun forzarActualizacion() {
        _recinto.value = _recinto.value // esto dispara los observers
        _actualizarUI.value = true
        actualizarCleaningLabel()
    }


    private fun actualizarCleaningLabel() {
        val recintoActual = _recinto.value ?: return
        val context = getApplication<Application>()

        // Asegurar que nunca mostramos días negativos
        val dias = recintoActual.diasEntreLimpiezas.coerceAtLeast(0)

        val texto = if (recintoActual.limpiezaHecha) {
            context.getString(es.uam.eps.dadm.faunary.R.string.clean_next, dias)
        } else {
            context.getString(es.uam.eps.dadm.faunary.R.string.clean_delay, dias)
        }

        _cleaningLabel.value = texto
    }




}