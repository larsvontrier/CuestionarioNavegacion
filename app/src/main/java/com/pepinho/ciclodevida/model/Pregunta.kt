package com.pepinho.pmdm.cuestionarios.model

import java.util.*

abstract class Pregunta(val enunciado: String, numero: Int) : Comparable<Pregunta> {
    companion object {
        const val DEFAULT_VALUE = 1
    }
    var idPregunta: Long = 0 // No se inicializa
    var descripcion: String? = null
    var numero: Int = DEFAULT_VALUE
        set(value) {
            field = if (value < 1) DEFAULT_VALUE else value
        }
    var puntos: Double = 1.0
        set(value) {
            field = if (value <= 0) DEFAULT_VALUE.toDouble() else value
        }

    init {
        this.numero = numero
    }

    abstract fun getCorrectIndex() : Int

    fun setDescripcion(descripcion: String) = apply { this.descripcion = descripcion }
    fun setNumero(numero: Int) = apply { this.numero = numero }
    fun setPuntos(puntos: Double) = apply { this.puntos = puntos }


    override fun toString() = if (enunciado.isEmpty()) "Pregunta sin enunciado"
    else "$numero. ${enunciado.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}"

    override fun compareTo(other: Pregunta) = this.numero - other.numero
}



