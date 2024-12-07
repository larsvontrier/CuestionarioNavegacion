package com.pepinho.ciclodevida

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.core.view.isVisible
import com.pepinho.ciclodevida.ResultadosActivity.Companion.INTENTOS_KEY
import com.pepinho.ciclodevida.databinding.ActivityMainBinding
import com.pepinho.ciclodevida.model.Resultado
import com.pepinho.ciclodevida.repositorio.PreguntaRepository
import com.pepinho.ciclodevida.repositorio.ResultadosRepository
import com.pepinho.pmdm.cuestionarios.model.PreguntaTest
import com.pepinho.pmdm.cuestionarios.model.PreguntaVerdaderoFalso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val preguntasRepository = PreguntaRepository
    private val resultadosRepository = ResultadosRepository
    private var numeroIntentos: Int = 0

    private var iActual: Int = 0

    companion object {
        const val INDICE = "indiceActual"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        if (savedInstanceState != null) {
            iActual = savedInstanceState.getInt(INDICE)
        }

        enableEdgeToEdge()

        numeroIntentos = intent.getIntExtra(INTENTOS_KEY, 0)
        binding.tvIntentos.text = getString(R.string.numero_intentos, numeroIntentos)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btLimpar.setOnClickListener {
            binding.rgOpciones.clearCheck()
            Toast.makeText(this, "Limpiada la selección", Toast.LENGTH_SHORT).show()

        }
        binding.btComprobar.setOnClickListener { checkAnswer() }

        binding.rgOpciones.setOnCheckedChangeListener { _, checkedId ->
            // Haz algo en respuesta al cambio de selección aquí
            val radioButton: RadioButton? = findViewById(checkedId)
            // Escribimos en el Logcat el id del botón de opción seleccionado y su texto.
            // Ten en cuenta que cuando se deselecciona un botón de opción, checkedId es -1 y radioButton es null
            Log.i("Boton con id: $checkedId", radioButton?.text.toString())
        }

        binding.btnNext.setOnClickListener {
            // Avanzamos a la siguiente pregunta
            iActual++
            updatePregunta()
        }

        binding.btEnviar.setOnClickListener {
            val intent = Intent(this, ResultadosActivity::class.java)
            intent.putExtra(ResultadosActivity.INTENTOS_KEY, numeroIntentos)
            startActivity(intent)
        }

        updatePregunta()
        Log.d("onCreate", "onCreate")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(INDICE, iActual)
        Log.d("Indice Salvado", "$iActual")
        Log.d("onSaveInstanceState", "onSaveInstanceState")
    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        iActual = savedInstanceState.getInt(INDICE)
//        Log.d("Indice Restaurado", "$iActual")
//        updatePregunta() // Se precisa actualizar, porque onCreate se llama antes que onRestoreInstanceState
//        Log.d("onRestoreInstanceState", "onRestoreInstanceState")
//    }

    /**
     * Actualiza la pregunta actual en la vista
     *
     */
    private fun updatePregunta() {
        val pregunta = preguntasRepository.getPreguntaByIndex(iActual) ?: return

        Log.d("Pregunta", pregunta.toString())
        binding.tvPregunta.text = String.format(
            getString(R.string.formatoEnunciado), iActual + 1,
            pregunta.enunciado
        )

        when (pregunta) {
            is PreguntaTest -> {
                updateRadioButtons(
                    pregunta.opciones.map { it?.enunciado ?: "-" }.toTypedArray(),
                    true
                )
            }

            is PreguntaVerdaderoFalso -> {
                updateRadioButtons(arrayOf("Verdadero", "Falso"), false)
            }

        }

        Log.d("Índice Actual", "$iActual")

//        binding.btnNext.isVisible = !preguntasRepository.isLastQuestionIndex(iActual)
        if (preguntasRepository.isLastQuestionIndex(iActual)) {
            binding.btnNext.isVisible = false
            binding.btEnviar.visibility = View.VISIBLE
        } else {
            binding.btnNext.isVisible = true
            binding.btEnviar.visibility = View.GONE
        }


    }

    /* Actualiza las opciones de los botones de radio con los textos proporcionados.
     * Si visible es true, se mostrarán los botones de radio 3 y 4, si no, se ocultarán.
     */
    private fun updateRadioButtons(textos: Array<String>, visible: Boolean) {

        binding.rgOpciones.clearCheck()

        binding.rbOpcion1.text = textos[0]
        binding.rbOpcion2.text = textos[1]
        binding.rbOpcion3.apply {
            text = textos.getOrNull(2) ?: ""
            isVisible = visible
        }
        binding.rbOpcion4.apply {
            text = textos.getOrNull(3) ?: ""
            isVisible = visible
        }

    }

    /**
     * Comprueba la respuesta seleccionada por el usuario, dependiendo del tipo de pregunta.
     */
    private fun checkAnswer() {
        val preguntaA = preguntasRepository.getPreguntaByIndex(iActual)
        if (preguntaA != null) {
            // El id de la opción seleccionada
            val seleccionada = binding.rgOpciones.checkedRadioButtonId
            if (seleccionada == -1) {
                Toast.makeText(this, "No has seleccionado ninguna opción", Toast.LENGTH_LONG)
                    .show()
            } else {
                val seleccionadaIndex =
                    binding.rgOpciones.indexOfChild(findViewById(seleccionada))
                val correcta = seleccionadaIndex == preguntaA.getCorrectIndex()
                resultadosRepository.addResultado(Resultado(preguntaA, correcta))

                Toast.makeText(
                    this,
                    if (correcta) "¡Correcto!" else "¡Incorrecto!",
                    Toast.LENGTH_LONG
                ).show()

            }
        }

    }

}