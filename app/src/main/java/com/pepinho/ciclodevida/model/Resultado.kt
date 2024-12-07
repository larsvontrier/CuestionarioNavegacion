package com.pepinho.ciclodevida.model

import com.pepinho.pmdm.cuestionarios.model.Pregunta

data class Resultado(val pregunta: Pregunta, val acierto: Boolean) {

    override fun toString() = "${pregunta.enunciado}: ${if (acierto) "Correcta" else "Incorrecta"}"

    override fun equals(other: Any?) = other is Resultado && pregunta == other.pregunta

    override fun hashCode() = pregunta.hashCode()
}
