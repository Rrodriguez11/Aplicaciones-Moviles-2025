package es.uam.eps.dadm.faunary.data

import android.content.Context
import es.uam.eps.dadm.faunary.FaunaryPrefs
import es.uam.eps.dadm.faunary.model.Animal
import es.uam.eps.dadm.faunary.model.Enfermedad
import es.uam.eps.dadm.faunary.model.Recinto

// Objeto singleton que actúa como repositorio de datos de los recintos y animales
object DataRepository {

    // Lista de recintos inicializados con animales y sus datos
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

    // Retorna un recinto por nombre, ignorando mayúsculas/minúsculas
    fun getRecintoByNombre(nombre: String): Recinto? {
        return habitats.find { it.nombre.equals(nombre, ignoreCase = true) }
    }

    // Retorna la lista completa de recintos
    fun getTodosLosRecintos(): List<Recinto> = habitats

    // Actualiza el estado diario de los recintos y los animales, simulando el paso de un día
    fun actualizarEstadoDiario(context: Context) {
        habitats.forEach { recinto ->

            // Lógica de limpieza del recinto
            if (recinto.limpiezaHecha) {
                recinto.diasEntreLimpiezas -= 1
                if (recinto.diasEntreLimpiezas <= 0) {
                    recinto.diasEntreLimpiezas = 0
                    recinto.limpiezaHecha = false
                }
            } else {
                recinto.diasEntreLimpiezas += 1
            }

            // Guardar el estado de limpieza en preferencias
            FaunaryPrefs.guardarDiasParaLimpieza(context, recinto.nombre, recinto.diasEntreLimpiezas)
            FaunaryPrefs.guardarEstadoLimpieza(context, recinto.nombre, recinto.limpiezaHecha)

            recinto.animales.forEach { animal ->

                // Lógica de alimentación
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

                // Lógica de enfermedades
                animal.enfermedades.forEach { enfermedad ->
                    if (!enfermedad.superada) {
                        if (!enfermedad.medicinaDada) {
                            // No medicado aún: el contador se mantiene en 0
                            enfermedad.diasHastaProximaMedicina = 0
                        } else {
                            // Disminuir el contador si ya se medicó
                            enfermedad.diasHastaProximaMedicina -= 1

                            if (enfermedad.diasHastaProximaMedicina <= 0) {
                                enfermedad.diasHastaProximaMedicina = 0
                                enfermedad.medicinaDada = false
                                FaunaryPrefs.guardarAnimalMedicado(context, recinto.nombre, animal.nombre)
                            }
                        }

                        // Guardar el estado actual de la enfermedad
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

    // Reinicia todos los datos a un estado base para propósitos de testeo
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
