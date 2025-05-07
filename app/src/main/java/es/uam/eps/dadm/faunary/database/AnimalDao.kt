package es.uam.eps.dadm.faunary.database

import androidx.room.Dao
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AnimalDao {

    @Insert
    suspend fun insert(animal: AnimalEntity): Long

    @Update
    suspend fun update(animal: AnimalEntity)

    @Delete
    suspend fun delete(animal: AnimalEntity)

    @Query("SELECT * FROM animales WHERE habitatId = :recintoId")
    fun getAnimalesPorHabitat(recintoId: Long): LiveData<List<AnimalEntity>>

    @Query("SELECT * FROM animales")
    fun getTodosLosAnimales(): LiveData<List<AnimalEntity>>
}