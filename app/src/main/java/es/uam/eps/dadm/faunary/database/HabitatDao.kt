package es.uam.eps.dadm.faunary.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HabitatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitat: HabitatEntity): Long

    @Update
    suspend fun update(habitat: HabitatEntity)

    @Delete
    suspend fun delete(habitat: HabitatEntity)

    @Query("SELECT * FROM habitats")
    fun getHabitats(): LiveData<List<HabitatEntity>>

    @Query("SELECT * FROM habitats WHERE id = :habitatId")
    suspend fun getHabitatPorId(habitatId: Long): HabitatEntity?

    @Query("SELECT * FROM habitats")
    suspend fun getHabitatsRaw(): List<HabitatEntity>

    @Query("UPDATE habitats SET limpiezaHecha = 1, diasEntreLimpiezas = diasEntreLimpiezasOriginal WHERE id = :habitatId")
    suspend fun markCleaningDone(habitatId: Long)
}