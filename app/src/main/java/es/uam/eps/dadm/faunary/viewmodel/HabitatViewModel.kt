package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.*
import es.uam.eps.dadm.faunary.FaunaryPrefs
import es.uam.eps.dadm.faunary.model.Animal
import es.uam.eps.dadm.faunary.model.Enfermedad
import es.uam.eps.dadm.faunary.model.Recinto
import timber.log.Timber
import es.uam.eps.dadm.faunary.data.DataRepository
import es.uam.eps.dadm.faunary.database.ZooDatabase
import es.uam.eps.dadm.faunary.database.AnimalEntity
import es.uam.eps.dadm.faunary.database.HabitatEntity
import kotlinx.coroutines.launch


/**
 * ViewModel asociado a la pantalla del hábitat.
 * Se encarga de almacenar y gestionar el estado relacionado con la limpieza,
 * alimentación y medicación de los animales.
 * Utiliza LiveData para actualizar automáticamente la interfaz.
 */
class HabitatViewModel(application: Application, private val habitatId: Long) : AndroidViewModel(application) {

    private val habitatDao = ZooDatabase.getDatabase(application).habitatDao()
    private val animalDao = ZooDatabase.getDatabase(application).animalDao()

    val habitat: LiveData<HabitatEntity?> = liveData {
        val result = habitatDao.getHabitatPorId(habitatId)
        emit(result)
    }

    val animales: LiveData<List<AnimalEntity>> = animalDao.getAnimalesPorHabitat(habitatId)

    val habitatName2 = "Sabana" // para que funcione de momento, ahora lo quito
    val habitatName: LiveData<String?> = habitat.map { it?.nombre }

    val fedCount: LiveData<Int> = animales.map { it.count { a -> !a.hambre } }
    val totalCount: LiveData<Int> = animales.map { it.size }
    //val sickCount: LiveData<Int> = animales.map { it.count { a -> a.enfermo } }
    //val medicatedCount: LiveData<Int> = animales.map { it.count { a -> a.enfermo && !a.necesitaMedicina } }
    val sickCount: LiveData<Int> = animales.map { it.count { a -> a.enfermo } }
    val medicatedCount: LiveData<Int> = animales.map { it.count { a -> a.enfermo} }

    // Indica si la limpieza ya ha sido realizada
    val cleaningDone: MutableLiveData<Boolean> = MutableLiveData(false)
    val cleaningLabel: MutableLiveData<String> = MutableLiveData()
    // Evento para mostrar un Toast tras realizar la limpieza
    val showCleaningToast: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        Timber.i("HabitatViewModel creado con id = $habitatId")

        viewModelScope.launch {
            val h = habitatDao.getHabitatPorId(habitatId)
            cleaningDone.postValue(h?.limpiezaHecha ?: false)
            actualizarCleaningLabel()
        }

    }


    /**
     * Marca la limpieza como realizada, lanza el evento del Toast y actualiza el estado.
     */
    fun markCleaningDone() {
        viewModelScope.launch {
            habitatDao.markCleaningDone(habitatId)
            cleaningDone.value = true

            actualizarCleaningLabel()
            showCleaningToast.value = true
            Timber.i("Habitat was cleaned successfully (estado guardado)")
        }

    }




    /**
     * Reinicia el evento del Toast tras mostrarlo una vez.
     */
    fun resetCleaningToast() {
        showCleaningToast.value = false
    }

    fun alimentarAnimal(animal: Animal) {
        animal.hambre = false
    }


    fun medicarAnimal(animal: Animal) {
        animal.medicar()
    }


    private val _actualizarUI = MutableLiveData<Boolean>()
    val actualizarUI: LiveData<Boolean> get() = _actualizarUI

    fun forzarActualizacion() {
        _actualizarUI.value = true
        actualizarCleaningLabel()
    }


    private fun actualizarCleaningLabel() {
        cleaningLabel.value = "A"
    }




}