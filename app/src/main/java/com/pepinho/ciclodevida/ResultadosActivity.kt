package com.pepinho.ciclodevida

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pepinho.ciclodevida.adapters.ResultadosAdapter
import com.pepinho.ciclodevida.databinding.ActivityResultadosBinding
import com.pepinho.ciclodevida.model.Resultado
import com.pepinho.ciclodevida.repositorio.ResultadosRepository
import kotlin.properties.Delegates

class ResultadosActivity : AppCompatActivity() {
    companion object {
        const val INTENTOS_KEY = "intentos"
    }

    var intento by Delegates.notNull<Int>()
    lateinit var binding: ActivityResultadosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

       intento = intent.getIntExtra(INTENTOS_KEY, 0)

        binding = ActivityResultadosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.lvResultado.adapter = ResultadosAdapter(this, R.layout.list_resultado_item,
            ResultadosRepository.resultados)

        binding.btRepetir.setOnClickListener {
            ResultadosRepository.clearResultados()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(INTENTOS_KEY, ++intento)
            startActivity(intent)
        }


    }

}