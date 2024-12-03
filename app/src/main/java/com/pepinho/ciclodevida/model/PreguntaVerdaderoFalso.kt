package com.pepinho.pmdm.cuestionarios.model

import java.util.function.Predicate

class PreguntaVerdaderoFalso(enunciado: String, numero: Int, var correcta: Boolean = true) :
    Pregunta(enunciado, numero), Predicate<Boolean> {

    override fun getCorrectIndex(): Int = if (correcta) 0 else 1

    override fun toString() = "${super.toString()} [${if (correcta) "V" else "F"}]"

    override fun test(t: Boolean) = t == correcta

}