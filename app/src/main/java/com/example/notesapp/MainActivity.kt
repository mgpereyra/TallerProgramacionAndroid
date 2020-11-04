package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.crearnotas.ui.CrearNotasActivity
import com.example.notas.ui.VerNotasActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navegarVerNotas()
        navegarCrearNotas()
    }

    private fun navegarVerNotas() {
        buttonVerNotas.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,
                VerNotasActivity::class.java)
            startActivity(intent) /*explicito navegar de un activity a otro activity*/
            /*implicito - navegar por gps, abrir camara*/
        })
    }

    private fun navegarCrearNotas() {
        buttonCrearNuevasNotas.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,
                CrearNotasActivity::class.java)
            startActivity(intent)
        })
    }
}
