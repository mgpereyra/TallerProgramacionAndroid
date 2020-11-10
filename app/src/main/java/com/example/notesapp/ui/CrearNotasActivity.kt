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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.notesapp.R
import kotlinx.android.synthetic.main.activity_crear_notas.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CrearNotasActivity : AppCompatActivity() {

    var db: com.example.notesapp.data.NotaDatabase? = null
    var conectado = false
    var pathImagen = ""
    private val REQUEST_CAMERA = 1002
    var photo:Uri?=null

    // * falta inyección
    // darle "cariño" a las vistas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_notas)
        comprobarPermisos()
        buscarGaleria()
        tomarFotos()
        cancelar()
        guardar()
    }

    private fun conectardb(){
        try{
            db = Room.databaseBuilder(
                applicationContext,
                com.example.notesapp.data.NotaDatabase::class.java, "database-notas"
            ).build()
            conectado=true
        }catch (e: Exception){
            Toast.makeText(applicationContext,"Error en con la base de datos...",Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardar() {
        btnGuardar.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p: View) {
                conectardb()
                if(conectado){
                    val descripcionNota = findViewById<EditText>(R.id.textDescription)
                    val descripcion = descripcionNota.getText().toString()
                    val miNota = com.example.notesapp.data.NotaEntity(
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
                            db?.notaDAO()?.insertarNota(miNota)
                        }
                        Toast.makeText(
                            applicationContext,
                            "Se ha guardado la nota",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    }
                }else{
                    Toast.makeText(applicationContext,"No estás conectado a la db",Toast.LENGTH_SHORT).show()
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
            openPhoto()
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

    /*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPhoto()
                } else {
                    Toast.makeText(this, "No podes abrir la camara", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    */

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