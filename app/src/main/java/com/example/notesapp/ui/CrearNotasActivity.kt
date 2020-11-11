package com.example.notesapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.NotesAppKoin
import com.example.notesapp.R
import com.example.notesapp.data.NotaDAO
import com.example.notesapp.data.NotaDAO_Impl
import com.example.notesapp.data.NotaDatabase
import com.example.notesapp.data.NotaEntity
import com.example.notesapp.databinding.ActivityCrearNotasBinding
import com.example.notesapp.databinding.ActivityVerNotasBinding
import com.example.notesapp.model.Nota
import kotlinx.android.synthetic.main.activity_crear_notas.*
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CrearNotasActivity : AppCompatActivity() {

    private val viewModel: NotasViewModel by viewModel()
    private lateinit var binding: ActivityCrearNotasBinding

    var pathImagen = ""
    private val REQUEST_CAMERA = 1002
    var photo:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearNotasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buscarGaleria()
        comprobarPermisos()
        tomarFotos()
        cancelar()
        guardar()
    }

    private fun guardar() {
        btnGuardar.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p: View) {
                val descripcionNota = findViewById<EditText>(R.id.textDescription)
                val descripcion = descripcionNota.getText().toString()
                val miNota = NotaEntity(
                    description = descripcion,
                    srcImagen = pathImagen
                )
                if(descripcion==""||pathImagen==""){
                    Toast.makeText(
                        applicationContext,
                        "Debe ingresar una descripción y una imagen",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    lifecycleScope.launch {
                        viewModel.insertarEnDatabase(miNota)
                    }
                    Toast.makeText(
                        applicationContext,
                        "Se ha guardado la nota",
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackPressed()
                }
            }
        })
    }

    private fun buscarGaleria() {
        buttonGaleria.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p: View) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 100)
            }
        })
    }

    private fun comprobarPermisos() {
        buttonSacarFotos.setOnClickListener {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    ||checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this,
                        "La cámara no está disponible ya que se denegaron los permisos necesarios...",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(this,
                    "Cámara no disponible en esta versión de Android...",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun tomarFotos(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                ||checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_DENIED){
                val permisosCamara = arrayOf(android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permisosCamara,REQUEST_CAMERA)
            }else{
                openPhoto()
            }
        }else{
            Toast.makeText(this,
                "Función no disponible en esta versión de Android...",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openPhoto() {
        val value = ContentValues()
        value.put(MediaStore.Images.Media.TITLE,"Nueva Imagen")
        photo=contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,value)
        buttonSacarFotos.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photo)
                startActivityForResult(camaraIntent,REQUEST_CAMERA)
            }
        })
    }

    private fun cancelar() {
        btnCancelar.setOnClickListener { onBackPressed() }
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
                Toast.makeText(this,"No se ha podido guardar la foto",Toast.LENGTH_SHORT).show()
            }
        }
    }

    lateinit var currentPhotoPath: String

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}