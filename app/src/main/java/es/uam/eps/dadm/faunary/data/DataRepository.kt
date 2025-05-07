package es.uam.eps.dadm.faunary.data

import es.uam.eps.dadm.faunary.model.Animal
import es.uam.eps.dadm.faunary.model.Enfermedad
import es.uam.eps.dadm.faunary.model.Recinto

object DataRepository {

    private val habitats = listOf(
        Recinto(
            nombre = "Sabana",
            tipo = "Terrestre - Carnívoros",
            capacidad = 5,
            diasEntreLimpiezas = 3,
            limpiezaHecha = false,
            animales = mutableListOf(
                Animal(
                    nombre = "Simba",
                    especie = "León",
                    fechaNacimiento = "2019-05-12",
                    peso = 180.0,
                    hambre = true,
                    enfermedades = mutableListOf(
                        Enfermedad("Pulgas", "2024-05-01", superada = false, medicinaDada = false)
                    )
                ),
                Animal(
                    nombre = "Nala",
                    especie = "Leona",
                    fechaNacimiento = "2020-08-03",
                    peso = 150.0,
                    hambre = false
                )
            )
        ),
        Recinto(
            nombre = "Polar",
            tipo = "Terrestre - Omnívoros",
            capacidad = 4,
            diasEntreLimpiezas = 2,
            limpiezaHecha = true,
            animales = mutableListOf(
                Animal(
                    nombre = "Koda",
                    especie = "Oso polar",
                    fechaNacimiento = "2018-11-01",
                    peso = 400.0,
                    hambre = true
                ),
                Animal(
                    nombre = "Kenai",
                    especie = "Oso polar",
                    fechaNacimiento = "2017-03-22",
                    peso = 430.0,
                    hambre = true,
                    enfermedades = mutableListOf(
                        Enfermedad("Resfriado", "2024-04-28", superada = false, medicinaDada = false)
                    )
                )
            )
        )
    )

    fun getRecintoByNombre(nombre: String): Recinto? {
        return habitats.find { it.nombre.equals(nombre, ignoreCase = true) }
    }

    fun getTodosLosRecintos(): List<Recinto> = habitats
}
