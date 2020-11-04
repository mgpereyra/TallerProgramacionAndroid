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
        levantarDatos()

        /*Setup ViewModels*/
        viewModel = ViewModelProviders.of(this).get(NotasViewModel::class.java)

        viewModel.getNotasList().observe(this, Observer {
            madapter.setearLista(it)
        })
    }

    fun levantarDatos(){
        conectardb()
        if(conectado){
            lifecycleScope.launch {
                var query = db?.notaDAO()?.getAll()?.forEach {
                    stringMostrar = stringMostrar + " " + it.id + " " + it.description
                    //textView.setText(stringMostrar)
                }
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