package com.pepinho.pmdm.cuestionarios.model

data class Opcion(val enunciado: String, val correcta: Boolean) {
    override fun toString() = if (correcta) "$enunciado [*]" else enunciado
}