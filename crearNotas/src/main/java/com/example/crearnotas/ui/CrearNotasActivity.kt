package com.example.crearnotas.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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

    var pathImagen = ""
    var db: NotaDatabase? = null
    var conectado = false

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

    private fun tomarFotos() {
        // PREGUNTAR POR LOS PERMISOS DE LA CÁMARA ANTES DE ESTO...
        if(intent.resolveActivity(this.packageManager)!=null) {
            buttonSacarFotos.setOnClickListener {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        // Create the File where the photo should go
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            null
                        }
                        // Continue only if the File was successfully created
                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                this,
                                "/storage/emulated/0/DCIM/Camera",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, 1)
                        }
                    }
                }
            }
        }else{
            // HACER QUE EL USUARIO ACTIVE EL PERMISO...
            Toast.makeText(this,"No se ha podido abrir la camara",Toast.LENGTH_SHORT).show()
        }
    }

    fun navegarHaciaAtras() {
        btnCancelar.setOnClickListener { onBackPressed() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            pathImagen=data!!.data.toString()
            imageView.setImageURI(data!!.data)
        }
        if (requestCode == 200 && resultCode == RESULT_OK) {
            try{
                createImageFile()
                val takeImage = data!!.extras!!.get("data") as Bitmap // Acá explota xd
                imageView.setImageBitmap(takeImage)
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