package es.uam.eps.dadm.faunary.model

import java.io.Serializable

data class Enfermedad(
    val nombre: String,
    val fecha: String,
    var superada: Boolean,
    var medicinaDada: Boolean,
    var frecuenciaMedicar: Int = 3,
    var diasHastaProximaMedicina: Int = frecuenciaMedicar
) : Serializable


data class Animal(
    var nombre: String,
    var especie: String,
    var fechaNacimiento: String,
    var peso: Double,
    var hambre: Boolean = true,
    var alimento: String = "Carne",
    var frecuenciaComida: Int = 2,
    var diasHastaProximaComida: Int = frecuenciaComida,
    var enfermedades: MutableList<Enfermedad> = mutableListOf()
) : Serializable {

    fun estaEnfermo(): Boolean = enfermedades.any { !it.superada }

    fun necesitaMedicina(): Boolean =
        enfermedades.any { !it.superada && !it.medicinaDada && it.diasHastaProximaMedicina == 0 }

    fun alimentar() {
        hambre = false
    }

    fun medicar() {
        enfermedades.forEach {
            if (!it.superada && !it.medicinaDada && it.diasHastaProximaMedicina == 0) {
                it.medicinaDada = true
                it.diasHastaProximaMedicina = it.frecuenciaMedicar
            }
        }
    }


    fun debeSerMedicadoHoy(): Boolean {
        return enfermedades.any {
            !it.superada && !it.medicinaDada && it.diasHastaProximaMedicina == 0
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
