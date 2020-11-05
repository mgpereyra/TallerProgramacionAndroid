package com.example.crearnotas.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.crearnotas.R
import com.example.crearnotas.data.NotaDAO
import com.example.crearnotas.data.NotaDatabase
import com.example.crearnotas.data.NotaEntity
import com.example.crearnotas.data.NotasApplication
import kotlinx.android.synthetic.main.activity_crear_notas.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CrearNotasActivity : AppCompatActivity() {

    var db: NotaDatabase? = null
    var conectado = false
    var pathImagen = ""
    private val REQUEST_CAMERA = 1002
    var photo:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_notas)
        buscarGaleria()
        tomarFotos()
        navegarHaciaAtras()
        cancelar()
        guardar()
    }

    private fun cancelar() {
        btnCancelar.setOnClickListener { onBackPressed() }
    }

    private fun conectardb(){
        try{
            db = Room.databaseBuilder(
                applicationContext,
                NotaDatabase::class.java, "database-notas"
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
                    var descripcionNota = findViewById<EditText>(R.id.textDescription)
                    var descripcion = descripcionNota.getText().toString()
                    var miNota = NotaEntity(description=descripcion, srcImagen = pathImagen)
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

    private fun tomarFotos(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                ||checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                /*Pedirle Permiso al usuario*/
                val permisosCamara = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

    fun navegarHaciaAtras() {
        btnCancelar.setOnClickListener { onBackPressed() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            pathImagen=data!!.data.toString()
            imageView.setImageURI(data!!.data)
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

    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = imageView.width
        val targetH: Int = imageView.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    }

}