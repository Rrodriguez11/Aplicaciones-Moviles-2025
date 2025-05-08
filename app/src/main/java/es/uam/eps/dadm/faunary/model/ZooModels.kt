package es.uam.eps.dadm.faunary.model

import java.io.Serializable

// Representa una enfermedad que puede tener un animal
data class Enfermedad(
    val nombre: String,                  // Nombre de la enfermedad
    val fecha: String,                  // Fecha de diagnóstico
    var superada: Boolean,              // Si la enfermedad ha sido superada
    var medicinaDada: Boolean,          // Si se ha dado medicina en el último turno
    var frecuenciaMedicar: Int = 3,     // Cada cuántos días se debe medicar
    var diasHastaProximaMedicina: Int = frecuenciaMedicar // Contador hasta la próxima dosis
) : Serializable

// Representa un animal en un recinto
data class Animal(
    var nombre: String,                        // Nombre del animal
    var especie: String,                       // Especie del animal
    var fechaNacimiento: String,              // Fecha de nacimiento
    var peso: Double,                          // Peso en kg
    var hambre: Boolean = true,                // Si tiene hambre o no
    var alimento: String = "Carne",            // Tipo de alimento que consume
    var frecuenciaComida: Int = 2,             // Cada cuántos días debe comer
    var diasHastaProximaComida: Int = frecuenciaComida, // Contador para su próxima comida
    var enfermedades: MutableList<Enfermedad> = mutableListOf() // Lista de enfermedades
) : Serializable {

    // Devuelve true si el animal tiene alguna enfermedad activa (no superada)
    fun estaEnfermo(): Boolean = enfermedades.any { !it.superada }

    // Devuelve true si el animal necesita medicina hoy (enfermo, no medicado, y contador en 0)
    fun necesitaMedicina(): Boolean =
        enfermedades.any { !it.superada && !it.medicinaDada && it.diasHastaProximaMedicina == 0 }

    // Marca al animal como alimentado
    fun alimentar() {
        hambre = false
    }

    // Aplica la medicina a todas las enfermedades activas que deben ser tratadas hoy
    fun medicar() {
        enfermedades.forEach {
            if (!it.superada && !it.medicinaDada && it.diasHastaProximaMedicina == 0) {
                it.medicinaDada = true
                it.diasHastaProximaMedicina = it.frecuenciaMedicar
            }
        }
    }

    // Verifica si el animal debe ser medicado hoy
    fun debeSerMedicadoHoy(): Boolean {
        return enfermedades.any {
            !it.superada && !it.medicinaDada && it.diasHastaProximaMedicina == 0
        }
    }
}

// Representa un recinto del zoológico
data class Recinto(
    val nombre: String,                               // Nombre del recinto (ej. "Sabana")
    val tipo: String,                                 // Tipo (ej. "Terrestre - Carnívoros")
    val capacidad: Int,                               // Máxima cantidad de animales
    var diasEntreLimpiezas: Int,                      // Días restantes hasta próxima limpieza
    val diasEntreLimpiezasOriginal: Int = diasEntreLimpiezas, // Valor original para reiniciar

    var limpiezaHecha: Boolean = false,               // Si la limpieza se realizó recientemente
    val animales: MutableList<Animal> = mutableListOf() // Lista de animales en el recinto
) {
    // Marca el recinto como recién limpiado
    fun limpiar() {
        limpiezaHecha = true
    }

    // Devuelve los animales que tienen hambre
    fun animalesConHambre(): List<Animal> = animales.filter { it.hambre }

    // Devuelve los animales enfermos (al menos una enfermedad no superada)
    fun animalesEnfermos(): List<Animal> = animales.filter { it.estaEnfermo() }
}
