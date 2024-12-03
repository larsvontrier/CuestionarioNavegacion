package com.pepinho.pmdm.cuestionarios.model

import java.util.function.Predicate

class PreguntaTest(enunciado: String, numero: Int, numOpciones: Int = NUMERO_OPCIONES) :
    Pregunta(enunciado, numero),
    Predicate<Int> {

    companion object {
        const val NUMERO_OPCIONES = 4
    }

    val opciones: Array<Opcion?> = Array(numOpciones) { null }

    init {
        require(numOpciones > 0) { "El número de opciones debe ser mayor que 0" } // Kotlin +1.5, lanza IllegalArgumentException
    }

    fun addOpcion(opcion: Opcion): Boolean {
        val index = opciones.indexOfFirst { it == null }
        return if (index != -1) {
            opciones[index] = opcion
            true
        } else {
            false
        }
    }

    fun getNumCorrectas(): Int = opciones.count { it?.correcta == true }

    fun getPuntos(marcadas: IntArray): Double {
        val numCorrectas = getNumCorrectas()
        if (numCorrectas == 0) return 0.0
        val marcadasBien = marcadas.count { opciones[it]?.correcta == true }
        val marcadasMal = marcadas.count { opciones[it]?.correcta == false }
        return (puntos * (marcadasBien - marcadasMal) / numCorrectas)
    }

    override fun getCorrectIndex() = opciones.indexOfFirst { it?.correcta == true }

    // Con MutableList:
//    fun getPuntos(marcadas: Array<Int>) = marcadas?.let { // let es una función de extensión que permite ejecutar un bloque de código si el objeto no es nulo
//        val marcadasBien = it.count { opciones[it]?.correcta == true }
//        val marcadasMal = it.count { opciones[it]?.correcta == false }
//        puntos * (marcadasBien - marcadasMal) / getNumCorrectas()
//    } ?: 0.0

//    override fun toString() = buildString {
//        append(super.toString()).append("\n")
//        opciones.forEachIndexed { index, opcion -> append("    ${index + 1}. ${opcion}\n") }
//    }
//
//    fun test(value: Int) = value in 0 until opciones.size && opciones[value]?.correcta == true

    override fun toString() = buildString {
        append(super.toString()).append("\n")
        opciones.forEachIndexed { index, opcion -> append("    ${index + 1}. ${opcion}\n") }
    }

    /*
    Con apply:
    override fun toString() = super.toString().apply {
        opciones.forEachIndexed { index, opcion -> append("    ${index + 1}. $opcion\n") }
    }
     */

    override fun test(value: Int) = value in opciones.indices && opciones[value]?.correcta == true
}
