package es.uam.eps.dadm.faunary.data

import android.content.Context
import es.uam.eps.dadm.faunary.FaunaryPrefs
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
                    frecuenciaComida = 2,
                    diasHastaProximaComida = 0,
                    enfermedades = mutableListOf(
                        Enfermedad(
                            nombre = "Pulgas",
                            fecha = "2024-05-01",
                            superada = false,
                            medicinaDada = false,
                            frecuenciaMedicar = 2,
                            diasHastaProximaMedicina = 0
                        )
                    )
                ),
                Animal(
                    nombre = "Nala",
                    especie = "Leona",
                    fechaNacimiento = "2020-08-03",
                    peso = 150.0,
                    hambre = true,
                    frecuenciaComida = 2,
                    diasHastaProximaComida = 0
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
                    hambre = true,
                    frecuenciaComida = 1,
                    diasHastaProximaComida = 0
                ),
                Animal(
                    nombre = "Kenai",
                    especie = "Oso polar",
                    fechaNacimiento = "2017-03-22",
                    peso = 430.0,
                    hambre = true,
                    frecuenciaComida = 1,
                    diasHastaProximaComida = 0,
                    enfermedades = mutableListOf(
                        Enfermedad(
                            nombre = "Resfriado",
                            fecha = "2024-04-28",
                            superada = false,
                            medicinaDada = false,
                            frecuenciaMedicar = 1,
                            diasHastaProximaMedicina = 0
                        )
                    )
                )
            )
        )
    )

    fun getRecintoByNombre(nombre: String): Recinto? {
        return habitats.find { it.nombre.equals(nombre, ignoreCase = true) }
    }

    fun getTodosLosRecintos(): List<Recinto> = habitats

    fun actualizarEstadoDiario(context: Context) {
        habitats.forEach { recinto ->
            if (recinto.limpiezaHecha) {
                recinto.diasEntreLimpiezas -= 1
                if (recinto.diasEntreLimpiezas <= 0) {
                    recinto.diasEntreLimpiezas = 0
                    recinto.limpiezaHecha = false
                }
            } else {
                recinto.diasEntreLimpiezas += 1
            }

            FaunaryPrefs.guardarDiasParaLimpieza(context, recinto.nombre, recinto.diasEntreLimpiezas)
            FaunaryPrefs.guardarEstadoLimpieza(context, recinto.nombre, recinto.limpiezaHecha)

            recinto.animales.forEach { animal ->
                if (animal.hambre) {
                    animal.diasHastaProximaComida = 0
                } else {
                    animal.diasHastaProximaComida -= 1
                    if (animal.diasHastaProximaComida <= 0) {
                        animal.hambre = true
                        animal.diasHastaProximaComida = 0
                        FaunaryPrefs.guardarAnimalAlimentado(context, recinto.nombre, animal.nombre, false)
                    }
                }

                animal.enfermedades.forEach { enfermedad ->
                    if (!enfermedad.superada) {
                        if (!enfermedad.medicinaDada) {
                            // Si aún no ha sido medicado, mantener contador en 0
                            enfermedad.diasHastaProximaMedicina = 0
                        } else {
                            // Reducir contador solo si ya ha sido medicado
                            enfermedad.diasHastaProximaMedicina -= 1

                            if (enfermedad.diasHastaProximaMedicina <= 0) {
                                enfermedad.diasHastaProximaMedicina = 0
                                enfermedad.medicinaDada = false
                                FaunaryPrefs.guardarAnimalMedicado(context, recinto.nombre, animal.nombre)
                            }
                        }

                        // Siempre guardar el contador actual (aunque sea 0)
                        FaunaryPrefs.guardarDiasHastaProximaMedicina(
                            context,
                            animal.nombre,
                            enfermedad.nombre,
                            enfermedad.diasHastaProximaMedicina
                        )
                    }
                }
            }
        }
    }

    fun reiniciarDatosParaTest() {
        habitats.forEach { recinto ->
            recinto.limpiezaHecha = false
            recinto.diasEntreLimpiezas = 0

            recinto.animales.forEach { animal ->
                animal.hambre = true
                animal.diasHastaProximaComida = 0

                animal.enfermedades.forEach { enfermedad ->
                    enfermedad.medicinaDada = false
                    enfermedad.diasHastaProximaMedicina = 0
                }
            }
        }
    }
}
