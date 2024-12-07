package com.pepinho.ciclodevida.repositorio

import com.pepinho.ciclodevida.model.Resultado

object ResultadosRepository{
    val resultados: MutableList<Resultado> = mutableListOf()
        get() = field

    fun addResultado(resultado: Resultado){
        resultados.add(resultado)
    }

    fun clearResultados(){
        resultados.clear()
    }

}