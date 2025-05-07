package es.uam.eps.dadm.faunary.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "animales",
    foreignKeys = [ForeignKey(
        entity = HabitatEntity::class,
        parentColumns = ["id"],
        childColumns = ["habitatId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("habitatId")]
)
data class AnimalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val habitatId: Long, // clave foránea al habitat
    val nombre: String,
    val especie: String,
    val fechaNacimiento: String,
    val peso: Double,
    val hambre: Boolean = true,
    val alimento: String,
    val diasParaComer: Int = 2
)