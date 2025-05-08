package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.uam.eps.dadm.faunary.database.HabitatDao
import es.uam.eps.dadm.faunary.database.HabitatEntity
import es.uam.eps.dadm.faunary.database.ZooDatabase

// ViewModel que gestiona la lógica relacionada con la selección de hábitats
class HabitatSelectionViewModel(application: Application) : AndroidViewModel(application) {

    // Acceso al DAO para interactuar con la base de datos de hábitats
    private val habitatDao: HabitatDao = ZooDatabase.getDatabase(application).habitatDao()

    // LiveData que expone la lista de hábitats desde la base de datos
    val habitats: LiveData<List<HabitatEntity>> = habitatDao.getHabitats()
}
