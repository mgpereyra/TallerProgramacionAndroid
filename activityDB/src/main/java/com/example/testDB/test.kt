package com.example.testDB

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.coroutines.launch

class test : AppCompatActivity() {
    var db: NotaDatabase? = null
    var conectado = false
    var stringMostrar = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        levantarDatos()
    }

    fun levantarDatos(){
        conectardb()
        if(conectado){
            lifecycleScope.launch {
            var query = db?.notaDAO()?.getAll()?.forEach {
                stringMostrar = stringMostrar + " " + it.id + " " + it.description
                textView.setText(stringMostrar)
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
            Toast.makeText(applicationContext,"Error al conectar con la base de datos...",Toast.LENGTH_SHORT).show()
        }
    }
}