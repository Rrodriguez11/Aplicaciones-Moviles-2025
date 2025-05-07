package es.uam.eps.dadm.faunary.model

import java.io.Serializable

data class Enfermedad(
    val nombre: String,
    val fecha: String,
    var superada: Boolean,
    var medicinaDada: Boolean,
    var diasParaMedicar: Int = 3
) : Serializable

data class Animal(
    var nombre: String,
    var especie: String,
    var fechaNacimiento: String,
    var peso: Double,
    var hambre: Boolean = true,
    var alimento: String = "Carne",
    var diasParaComer: Int = 2,
    var enfermedades: MutableList<Enfermedad> = mutableListOf()
) : Serializable {
    fun estaEnfermo(): Boolean = enfermedades.any { !it.superada }

    fun necesitaMedicina(): Boolean =
        enfermedades.any { !it.superada && !it.medicinaDada }

    fun alimentar() {
        hambre = false
    }

    fun medicar() {
        enfermedades.forEach {
            if (!it.superada && !it.medicinaDada) {
                it.medicinaDada = true
            }
        }
    }
}

data class Recinto(
    val nombre: String,
    val tipo: String,
    val capacidad: Int,
    var diasEntreLimpiezas: Int,
    val diasEntreLimpiezasOriginal: Int = diasEntreLimpiezas,

    var limpiezaHecha: Boolean = false,
    val animales: MutableList<Animal> = mutableListOf()
) {
    fun limpiar() {
        limpiezaHecha = true
    }

    fun animalesConHambre(): List<Animal> = animales.filter { it.hambre }

    fun animalesEnfermos(): List<Animal> = animales.filter { it.estaEnfermo() }
}
