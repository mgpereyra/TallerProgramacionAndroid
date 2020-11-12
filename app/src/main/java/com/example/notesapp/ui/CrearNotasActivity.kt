package com.example.notesapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityCrearNotasBinding
import kotlinx.android.synthetic.main.activity_crear_notas.*
import org.koin.android.viewmodel.ext.android.viewModel

class CrearNotasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearNotasBinding
    private val viewModelCrear: CrearNotasViewModel by viewModel()

    var pathImagen = ""
    private val REQUEST_CAMERA = 1002
    var photo:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearNotasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelCrear.comprobarPermisos(this)
        binding.buttonSacarFotos.setOnClickListener { viewModelCrear.tomarFotos(this) }
        binding.btnGuardar.setOnClickListener { viewModelCrear.guardar(this) }
        binding.buttonGaleria.setOnClickListener { viewModelCrear.buscarGaleria(this) }
        binding.btnCancelar.setOnClickListener { onBackPressed() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            pathImagen=data!!.data.toString()
            imageView.setImageURI(data.data)
        }
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            try{
                imageView.setImageURI(photo)
                pathImagen=photo.toString()
            }catch(e: Exception){
                Toast.makeText(this,
                    R.string.foto_no_guardada,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}