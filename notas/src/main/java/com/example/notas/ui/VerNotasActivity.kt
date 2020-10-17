package com.example.notas.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crearnotas.CrearNotasActivity
import com.example.notas.R
import com.example.notas.viewmodel.NotasViewModel

import kotlinx.android.synthetic.main.activity_ver_notas.*
import kotlinx.android.synthetic.main.notas_items.*

class VerNotasActivity : AppCompatActivity() {

    var madapter: NotasAdapter = NotasAdapter()
    private lateinit var viewModel: NotasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_notas)

        recyclerView.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this@VerNotasActivity)
            recyclerView.adapter = madapter
        }

        if (baseContext.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager =
                LinearLayoutManager(this) /*Si esta en orientacion Portrait aplica un LinearLayout*/
        }
        else{
            recyclerView.layoutManager =  LinearLayoutManager(this)
        }

        navegarHaciaAtras()

        /*Setup ViewModels*/
        viewModel = ViewModelProviders.of(this).get(NotasViewModel::class.java)

        viewModel.getNotasList().observe(this, Observer {
            madapter.setearLista(it)
        })
    }

    private fun navegarHaciaAtras() {
        btnVolver.setOnClickListener { onBackPressed() }
    }
}