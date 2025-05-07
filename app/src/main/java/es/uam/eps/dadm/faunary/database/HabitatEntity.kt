package es.uam.eps.dadm.faunary.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitats")
data class HabitatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,
    val tipo: String,
    val capacidad: Int,
    val diasEntreLimpiezas: Int,
    val diasEntreLimpiezasOriginal: Int = diasEntreLimpiezas,
    val limpiezaHecha: Boolean = false
)
