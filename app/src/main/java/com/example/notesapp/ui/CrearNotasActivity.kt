package com.example.notesapp.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.notesapp.R
import com.example.notesapp.data.NotaEntity
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
        binding.buttonSacarFotos.setOnClickListener { tomarFotos() }
        binding.btnGuardar.setOnClickListener { guardar() }
        binding.buttonGaleria.setOnClickListener { buscarGaleria() }
        binding.btnCancelar.setOnClickListener { onBackPressed() }
    }

    fun buscarGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100, Bundle())
    }

    fun tomarFotos() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                ||checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_DENIED){
                val permisosCamara = arrayOf(android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permisosCamara,REQUEST_CAMERA)
            }else{
                val value = ContentValues()
                photo = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,value)
                val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photo)
                startActivityForResult(
                    camaraIntent,
                    REQUEST_CAMERA,
                    Bundle()
                )
            }
        }else{
            Toast.makeText(applicationContext,
                getString(R.string.funcion_no_disponible),
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun guardar() {
        val descripcion = textDescription.text.toString()
        if(descripcion.trim()=="" || pathImagen==""){
            Toast.makeText(
                applicationContext,
                getString(R.string.debe_ingresar_una_descripcion_y_una_imagen),
                Toast.LENGTH_SHORT
            ).show()
        }else{
            viewModelCrear.insertarEnDatabase(
                NotaEntity(
                    description = descripcion,
                    srcImagen = pathImagen
                )
            )
            Toast.makeText(
                applicationContext,
                getString(R.string.nota_guardada),
                Toast.LENGTH_SHORT
            ).show()
            onBackPressed()
        }
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