package com.pepinho.ciclodevida.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.pepinho.ciclodevida.R
import com.pepinho.ciclodevida.databinding.ListResultadoItemBinding
import com.pepinho.ciclodevida.model.Resultado

class ResultadosAdapter(contexto: Context, resourceId: Int, resultados: MutableList<Resultado>):
    ArrayAdapter<Resultado>(contexto, resourceId, resultados) {

    /**
     * Método que se encarga de devolver la vista de un elemento de la lista
     * @param position posición del elemento en la lista
     * @param convertView vista que se va a reciclar, que se corresponde con cada elemento de la lista
     * @param parent vista padre, el listView
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding: ListResultadoItemBinding
        var vistaResultado: View
        if(convertView==null){ // Si no se puede reciclar la vista
            binding = ListResultadoItemBinding.inflate(LayoutInflater.from(context))
            vistaResultado = binding.root // Es la vista del elemento de la lista
            vistaResultado.tag = binding // Guardamos el binding en la vista para poder reciclarla

        } else {
            binding = convertView.tag as ListResultadoItemBinding
            vistaResultado = convertView
        }

        val resultado = getItem(position)

        binding.ivAcierto.setImageResource(if(resultado?.acierto == true) R.drawable.ic_ok else R.drawable.ic_cancel)
        "${position+1}.- ${resultado?.pregunta?.enunciado.orEmpty().substring(0..24)}".also { binding.tvPregunta.text = it }

        return vistaResultado
    }
}