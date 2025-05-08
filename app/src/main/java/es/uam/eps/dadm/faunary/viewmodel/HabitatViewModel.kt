package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.*
import es.uam.eps.dadm.faunary.FaunaryPrefs
import es.uam.eps.dadm.faunary.model.Animal
import es.uam.eps.dadm.faunary.model.Recinto
import es.uam.eps.dadm.faunary.data.DataRepository
import timber.log.Timber

/**
 * ViewModel para la pantalla de hábitat.
 * Gestiona el estado de alimentación, medicación y limpieza del recinto.
 * Usa LiveData para reflejar cambios en la interfaz automáticamente.
 */
class HabitatViewModel(application: Application, val habitatName: String) : AndroidViewModel(application) {

    // Recinto actual cargado desde el repositorio (seguro de no ser null)
    private val _recinto = MutableLiveData<Recinto>().apply {
        value = DataRepository.getRecintoByNombre(habitatName)
        requireNotNull(value) { "Recinto no encontrado: $habitatName" }
    }
    val recinto: LiveData<Recinto> get() = _recinto

    // Conteo de animales alimentados
    private val _fedCount = MutableLiveData(
        _recinto.value?.animales?.count { !it.hambre } ?: 0
    )
    val fedCount: LiveData<Int> get() = _fedCount

    // Total de animales que deben comer hoy
    val totalCount: LiveData<Int> = MutableLiveData(
        _recinto.value?.animales?.size ?: 0
    )

    // Conteo de animales enfermos y medicados (que no necesitan medicina)
    val sickCount: LiveData<Int> = MutableLiveData(
        _recinto.value?.animales?.count { it.estaEnfermo() } ?: 0
    )
    val medicatedCount: LiveData<Int> = MutableLiveData(
        _recinto.value?.animales?.count { it.estaEnfermo() && !it.necesitaMedicina() } ?: 0
    )

    // LiveData para control de limpieza
    val cleanDelayDays = MutableLiveData<Int>(2)
    val cleanFrequency = MutableLiveData<Int>(4)
    private val _cleaningLabel = MutableLiveData<String>()
    val cleaningLabel: LiveData<String> get() = _cleaningLabel

    // Condición si todos los animales están alimentados
    val todosAlimentados: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(fedCount) { value = fedCount.value == totalCount.value }
        addSource(totalCount) { value = fedCount.value == totalCount.value }
    }

    // Condición si todos los animales están medicados (no requieren medicación)
    val todosMedicados: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(medicatedCount) { value = medicatedCount.value == sickCount.value }
        addSource(sickCount) { value = medicatedCount.value == sickCount.value }
    }

    // Condición final: animales están en buen estado si están alimentados y medicados
    val animalesEnBuenEstado: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        fun actualizar() {
            value = (todosAlimentados.value == true && todosMedicados.value == true)
        }
        addSource(todosAlimentados) { actualizar() }
        addSource(todosMedicados) { actualizar() }
    }

    private val _cleaningDone = MutableLiveData(false)
    val cleaningDone: LiveData<Boolean> get() = _cleaningDone

    private val _showCleaningToast = MutableLiveData<Boolean>()
    val showCleaningToast: LiveData<Boolean> get() = _showCleaningToast

    init {
        val recintoCargado = DataRepository.getRecintoByNombre(habitatName)
        requireNotNull(recintoCargado)

        // Restaurar estado de limpieza y contadores desde preferencias
        recintoCargado.limpiezaHecha = FaunaryPrefs.obtenerEstadoLimpieza(getApplication(), recintoCargado.nombre)
        FaunaryPrefs.obtenerDiasParaLimpieza(getApplication(), recintoCargado.nombre)?.let {
            recintoCargado.diasEntreLimpiezas = it
        }

        _recinto.value = recintoCargado
        _cleaningDone.value = recintoCargado.limpiezaHecha
        actualizarCleaningLabel()

        Timber.i("HabitatViewModel creado con limpieza restaurada: ${recintoCargado.limpiezaHecha}")

        // Restaurar estados individuales de animales desde preferencias
        recintoCargado.animales.forEach { animal ->
            val alimentado = FaunaryPrefs.estaAnimalAlimentado(getApplication(), recintoCargado.nombre, animal.nombre)
            animal.hambre = !alimentado

            if (FaunaryPrefs.estaAnimalMedicado(getApplication(), recintoCargado.nombre, animal.nombre)) {
                animal.enfermedades.forEach { it.medicinaDada = true }
            }

            animal.enfermedades.forEach { enfermedad ->
                val diasRestantes = FaunaryPrefs.obtenerDiasHastaProximaMedicina(
                    getApplication(),
                    animal.nombre,
                    enfermedad.nombre
                ) ?: enfermedad.diasHastaProximaMedicina

                enfermedad.diasHastaProximaMedicina = diasRestantes
            }
        }

        // Calcular conteos solo para animales que deben comer/medicarse hoy
        val animalesParaComer = recintoCargado.animales.filter { it.diasHastaProximaComida == 0 }
        _fedCount.value = animalesParaComer.count { !it.hambre }
        (totalCount as MutableLiveData).value = animalesParaComer.size

        val animalesParaMedicar = recintoCargado.animales.filter { it.debeSerMedicadoHoy() }
        (sickCount as MutableLiveData).value = animalesParaMedicar.size
        (medicatedCount as MutableLiveData).value =
            animalesParaMedicar.count { !it.necesitaMedicina() }
    }

    /** Marca la limpieza como hecha, guarda en prefs, actualiza UI y lanza evento de Toast */
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

    /** Resetea el flag del Toast para evitar que se muestre de nuevo sin motivo */
    fun resetCleaningToast() {
        _showCleaningToast.value = false
    }

    /** Alimenta un animal específico, actualiza su estado y persiste en prefs */
    fun alimentarAnimal(animal: Animal) {
        animal.hambre = false
        animal.diasHastaProximaComida = animal.frecuenciaComida
        _fedCount.value = _recinto.value?.animales?.count { !it.hambre }

        _recinto.value?.let {
            FaunaryPrefs.guardarAnimalAlimentado(getApplication(), it.nombre, animal.nombre, true)
        }
    }

    /** Medica a un animal, actualiza y guarda estado de cada enfermedad */
    fun medicarAnimal(animal: Animal) {
        animal.medicar()

        _recinto.value?.let { recinto ->
            animal.enfermedades.forEach {
                if (!it.superada && it.medicinaDada) {
                    FaunaryPrefs.guardarAnimalMedicado(getApplication(), recinto.nombre, animal.nombre)
                    FaunaryPrefs.guardarDiasHastaProximaMedicina(
                        getApplication(),
                        animal.nombre,
                        it.nombre,
                        it.diasHastaProximaMedicina
                    )
                }
            }
        }

        (medicatedCount as MutableLiveData).value =
            _recinto.value?.animales?.count { it.estaEnfermo() && !it.necesitaMedicina() } ?: 0

        _recinto.value = _recinto.value // Forzar recomposición de la UI
    }

    private val _actualizarUI = MutableLiveData<Boolean>()
    val actualizarUI: LiveData<Boolean> get() = _actualizarUI

    /** Recalcula todo el estado desde SharedPreferences */
    fun forzarActualizacion() {
        _recinto.value?.let { recinto ->
            val nombre = recinto.nombre

            recinto.limpiezaHecha = FaunaryPrefs.obtenerEstadoLimpieza(getApplication(), nombre)
            recinto.diasEntreLimpiezas = FaunaryPrefs.obtenerDiasParaLimpieza(getApplication(), nombre)
                ?: recinto.diasEntreLimpiezasOriginal

            recinto.animales.forEach { animal ->
                animal.hambre = !FaunaryPrefs.estaAnimalAlimentado(getApplication(), nombre, animal.nombre)

                if (FaunaryPrefs.estaAnimalMedicado(getApplication(), nombre, animal.nombre)) {
                    animal.enfermedades.forEach { it.medicinaDada = true }
                }

                animal.enfermedades.forEach { enfermedad ->
                    val diasRestantes = FaunaryPrefs.obtenerDiasHastaProximaMedicina(
                        getApplication(), animal.nombre, enfermedad.nombre
                    )
                    if (diasRestantes != null) {
                        enfermedad.diasHastaProximaMedicina = diasRestantes
                    }
                }
            }

            val animalesParaComer = recinto.animales.filter { it.diasHastaProximaComida == 0 }
            _fedCount.value = animalesParaComer.count { !it.hambre }
            (totalCount as MutableLiveData).value = animalesParaComer.size

            val animalesParaMedicar = recinto.animales.filter { it.debeSerMedicadoHoy() }
            (medicatedCount as MutableLiveData).value = animalesParaMedicar.count { !it.necesitaMedicina() }
            (sickCount as MutableLiveData).value = animalesParaMedicar.size
        }

        _recinto.value = _recinto.value
        _cleaningDone.value = _recinto.value?.limpiezaHecha
        actualizarCleaningLabel()
        _actualizarUI.value = true
    }

    /** Actualiza el mensaje de limpieza según el estado actual del recinto */
    private fun actualizarCleaningLabel() {
        val recintoActual = _recinto.value ?: return
        val context = getApplication<Application>()
        val dias = recintoActual.diasEntreLimpiezas.coerceAtLeast(0)

        val texto = if (recintoActual.limpiezaHecha) {
            context.getString(es.uam.eps.dadm.faunary.R.string.clean_next, dias)
        } else {
            context.getString(es.uam.eps.dadm.faunary.R.string.clean_delay, dias)
        }

        _cleaningLabel.value = texto
    }
}
