package com.example.notas.ui

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.notas.R
import com.example.notas.data.NotaDatabase
import com.example.notas.data.NotaEntity
import com.example.notas.viewmodel.NotasViewModel
import kotlinx.android.synthetic.main.activity_ver_notas.*
import kotlinx.coroutines.launch

class VerNotasActivity : AppCompatActivity() {
    var db: NotaDatabase? = null
    var conectado = false
    var stringMostrar = ""
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
        levantarDatos(this)
    }
/*
    private fun borrar() {
        conectardb()
        lifecycleScope.launch {
            var notas: MutableList<NotaEntity> = mutableListOf()
            var query = db?.notaDAO()?.deleteAll()
            }
    }
*/
    fun levantarDatos(verNotasActivity: VerNotasActivity) {
        conectardb()
        if(conectado){
            lifecycleScope.launch {
                // CREAR LISTA
                var notas: MutableList<NotaEntity> = mutableListOf()
                var query = db?.notaDAO()?.getAll()?.forEach {
                    //stringMostrar = stringMostrar + " " + it.id + " " + it.description
                    //textView.setText(stringMostrar)

                    //AÑADIR NOTAS A LA LISTA
                    notas.add(it)
                }
                // PASARLE LISTA AL VIEWMODEL
                /*Setup ViewModels*/
                notas.reverse()
                viewModel = ViewModelProviders.of(verNotasActivity).get(NotasViewModel::class.java)
                viewModel.getNotasList().observe(verNotasActivity, Observer {
                    madapter.setearLista(notas)
                })
            }
        }else{
            Toast.makeText(applicationContext,
                "Error al conectar con la base de datos...",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun conectardb(){
        try{
            db = Room.databaseBuilder(
                applicationContext,
                NotaDatabase::class.java, "database-notas"
            ).build()
            conectado=true
        }catch (e: Exception){
            Toast.makeText(applicationContext,"Error al conectar con la base de datos...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navegarHaciaAtras() {
        btnVolver.setOnClickListener { onBackPressed() }
    }
}