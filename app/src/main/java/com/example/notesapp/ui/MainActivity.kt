package com.example.notesapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.notesapp.R
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
            val intent = Intent(this, VerNotasActivity::class.java)
            startActivity(intent)
        })
    }

    private fun navegarCrearNotas() {
        buttonCrearNuevasNotas.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CrearNotasActivity::class.java)
            startActivity(intent)
        })
    }
}
