package es.uam.eps.dadm.faunary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import es.uam.eps.dadm.faunary.database.HabitatDao
import es.uam.eps.dadm.faunary.database.HabitatEntity
import es.uam.eps.dadm.faunary.database.ZooDatabase

class HabitatSelectionViewModel(application: Application) : AndroidViewModel(application) {

    private val habitatDao: HabitatDao = ZooDatabase.getDatabase(application).habitatDao()

    val habitats: LiveData<List<HabitatEntity>> = habitatDao.getHabitats()
}