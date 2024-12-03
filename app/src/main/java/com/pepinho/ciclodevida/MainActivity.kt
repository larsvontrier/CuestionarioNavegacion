package com.pepinho.ciclodevida

import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.core.view.isVisible
import com.pepinho.ciclodevida.databinding.ActivityMainBinding
import com.pepinho.ciclodevida.repositorio.PreguntaRepository
import com.pepinho.pmdm.cuestionarios.model.Pregunta
import com.pepinho.pmdm.cuestionarios.model.PreguntaTest
import com.pepinho.pmdm.cuestionarios.model.PreguntaVerdaderoFalso

class MainActivity : AppCompatActivity() {

//    private lateinit var radioButtons: List<RadioButton>

    private lateinit var binding: ActivityMainBinding

    private val preguntasRepository = PreguntaRepository

    private var iActual: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
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
            iActual++
            updatePregunta()
        }

        updatePregunta()

    }

    /**
     * Actualiza la pregunta actual en la vista
     *
     */
    private fun updatePregunta() {
        val pregunta = preguntasRepository.getPreguntaByIndex(iActual) ?: return

        Log.d("Pregunta", pregunta.toString())
        binding.tvPregunta.text = String.format(getString(R.string.formatoEnunciado), iActual+1,
            pregunta.enunciado)

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

        binding.btnNext.isVisible = !preguntasRepository.isLastQuestionIndex(iActual)

    }

    /* Actualiza las opciones de los botones de radio con los textos proporcionados.
     * Si visible es true, se mostrarán los botones de radio 3 y 4, si no, se ocultarán.
     */
    private fun updateRadioButtons(textos: Array<String>, visible: Boolean) {
        binding.rbOpcion1.apply {
            text = textos[0]
            isChecked = false // El estado del botón se conserva por lo que hay que desmarcarlo
        }
        binding.rbOpcion2.apply {
            text = textos[1]
            isChecked = false
        }
        binding.rbOpcion3.apply {
            if (textos.size > 2) text = textos[2]
            isVisible = visible
            isChecked = false
        }
        binding.rbOpcion4.apply {
            if (textos.size > 2) text = textos[3]
            isVisible = visible
            isChecked = false
        }

    }

    /**
     * Comprueba la respuesta seleccionada por el usuario, dependiendo del tipo de pregunta.
     */
    private fun checkAnswer() {
        val preguntaA = preguntasRepository.getPreguntaByIndex(iActual)
        when (preguntaA) {
            is PreguntaTest -> {
                // El id de la opción seleccionada
                val seleccionada = binding.rgOpciones.checkedRadioButtonId
                if (seleccionada == -1) {
                    Toast.makeText(this, "No has seleccionado ninguna opción", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val seleccionadaIndex =
                        binding.rgOpciones.indexOfChild(findViewById(seleccionada))
                    if (seleccionadaIndex == preguntaA.getCorrectIndex()) {
                        Toast.makeText(this, "¡Correcto!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "¡Incorrecto!", Toast.LENGTH_LONG).show()
                    }
                }
            }

            is PreguntaVerdaderoFalso -> {
                val seleccionada = binding.rgOpciones.checkedRadioButtonId
                if (seleccionada == -1) {
                    Toast.makeText(this, "No has seleccionado ninguna opción", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val seleccionadaIndex =
                        binding.rgOpciones.indexOfChild(findViewById(seleccionada))
                    Toast.makeText(
                        this,
                        if (seleccionadaIndex == preguntaA.getCorrectIndex()) "¡Correcto!"
                        else "¡Incorrecto!", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}