package es.uam.eps.dadm.faunary.model

import java.io.Serializable

data class Enfermedad(
    val nombre: String,
    val fecha: String,
    var superada: Boolean,
    var medicinaDada: Boolean
) : Serializable

data class Animal(
    var nombre: String,
    var especie: String,
    var fechaNacimiento: String,
    var peso: Double,
    var hambre: Boolean = true,
    var alimento: String = "Carne",
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
    val diasEntreLimpiezas: Int,
    var limpiezaHecha: Boolean = false,
    val animales: MutableList<Animal> = mutableListOf()
) {
    fun limpiar() {
        limpiezaHecha = true
    }

    fun animalesConHambre(): List<Animal> = animales.filter { it.hambre }

    fun animalesEnfermos(): List<Animal> = animales.filter { it.estaEnfermo() }
}
