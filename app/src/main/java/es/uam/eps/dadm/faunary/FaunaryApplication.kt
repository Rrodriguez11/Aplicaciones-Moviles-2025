package es.uam.eps.dadm.faunary

import android.app.Application
import es.uam.eps.dadm.faunary.database.ZooDatabase
import es.uam.eps.dadm.faunary.database.AnimalEntity
import es.uam.eps.dadm.faunary.database.HabitatEntity
import es.uam.eps.dadm.faunary.data.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Clase de aplicación de Faunary.
 * Se ejecuta al iniciar la app, antes que cualquier actividad.
 */
class FaunaryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())


        // deleteDatabase("zoo_database")

        // Forzar creación de base de datos
        val db = ZooDatabase.getDatabase(this)
        Timber.i("Base de datos accedida: ${db.openHelper.databaseName}")

        // Insertar habitat de prueba si está vacía
        CoroutineScope(Dispatchers.IO).launch {
            val habitatDao = db.habitatDao()
            val animalDao = db.animalDao()

            if (habitatDao.getHabitatsRaw().isEmpty()) {

                // Sabana
                val sabanaId = habitatDao.insert(
                    HabitatEntity(
                        nombre = "Sabana",
                        tipo = "Terrestre - Carnívoros",
                        capacidad = 5,
                        diasEntreLimpiezas = 3,
                        limpiezaHecha = false
                    )
                )
                animalDao.insert(
                    AnimalEntity(
                        habitatId = sabanaId,
                        nombre = "Simba",
                        especie = "León",
                        fechaNacimiento = "2019-05-12",
                        peso = 180.0,
                        hambre = true,
                        alimento = "Carne"
                    )
                )
                animalDao.insert(
                    AnimalEntity(
                        habitatId = sabanaId,
                        nombre = "Nala",
                        especie = "Leona",
                        fechaNacimiento = "2020-08-03",
                        peso = 150.0,
                        hambre = false,
                        alimento = "Carne"
                    )
                )

                // Polar
                val polarId = habitatDao.insert(
                    HabitatEntity(
                        nombre = "Polar",
                        tipo = "Terrestre - Omnívoros",
                        capacidad = 4,
                        diasEntreLimpiezas = 2,
                        limpiezaHecha = true
                    )
                )
                animalDao.insert(
                    AnimalEntity(
                        habitatId = polarId,
                        nombre = "Koda",
                        especie = "Oso polar",
                        fechaNacimiento = "2018-11-01",
                        peso = 400.0,
                        hambre = true,
                        alimento = "Carne"
                    )
                )
                animalDao.insert(
                    AnimalEntity(
                        habitatId = polarId,
                        nombre = "Kenai",
                        especie = "Oso polar",
                        fechaNacimiento = "2017-03-22",
                        peso = 430.0,
                        hambre = true,
                        alimento = "Carne"
                    )
                )

                Timber.i("Recintos iniciales insertados")
            }
        }

        if (DateUtils.haPasadoUnDia(this)) {
            DataRepository.actualizarEstadoDiario(this)
            Timber.i("Nuevo día detectado: estado actualizado")
        } else {
            Timber.i("Mismo día: no se actualizó el estado")
        }
    }
}