package com.pepinho.pmdm.cuestionarios.model

class Cuestionario(val preguntas: MutableList<Pregunta>) {

    constructor(ps: Array<Pregunta>) : this(ps.toMutableList())
    val size: Int
        get() = preguntas.size

    fun realizarExamen(marcadas: Array<Any>): Double {
        var puntuacion = 0.0

        preguntas.forEachIndexed { index, pregunta ->
            val marcada = marcadas[index]
            puntuacion += when (pregunta) {
                is PreguntaTest -> pregunta.getPuntos(marcada as IntArray)
                is PreguntaVerdaderoFalso -> if (pregunta.test(marcada as Boolean)) pregunta.puntos else 0.0
                else -> 0.0
            }
        }
        return puntuacion
    }

    //
    fun addPregunta(pregunta: Pregunta): Boolean = preguntas.add(pregunta)
    fun removePregunta(pregunta: Pregunta): Boolean = preguntas.remove(pregunta)
    fun removePregunta(numero: Int): Boolean = preguntas.removeIf { it.numero == numero }
    fun getPregunta(numero: Int): Pregunta? = preguntas[numero]

    fun isLast(numero: Int) = numero == preguntas.size-1


    fun setPreguntas(preguntas: List<Pregunta>) {
        this.preguntas.clear()
        this.preguntas.addAll(preguntas)
    }

    fun clearPreguntas() = preguntas.clear()
    fun getNumPreguntas(): Int = preguntas.size
    fun size() = preguntas.size
    fun isEmpty() = preguntas.isEmpty()
    fun isNotEmpty() = preguntas.isNotEmpty()

    override fun toString() = "Examen:\n" + preguntas.joinToString("\n")

}