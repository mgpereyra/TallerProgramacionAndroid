package com.example.notesapp.ui

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityCrearNotasBinding
import com.example.notesapp.model.Nota
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_crear_notas.*
import kotlinx.android.synthetic.main.notas_items.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class CrearNotasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearNotasBinding
    private val viewModelCrear: CrearNotasViewModel by viewModel()


    private val REQUEST_GALERIA = 1000
    private val REQUEST_CAMERA = 2000
    private var photo: Uri? = null
    private var pathImagen = ""
    private var nota: Nota? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val parametros = this.intent.getExtras()
        super.onCreate(savedInstanceState)
        binding = ActivityCrearNotasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(parametros!=null){
            cargarNota()
        }

        binding.buttonSacarFotos.setOnClickListener { tomarFotos() }
        binding.btnGuardar.setOnClickListener { guardar() }
        binding.buttonGaleria.setOnClickListener { buscarGaleria() }
        binding.btnCancelar.setOnClickListener { onBackPressed() }
    }

    private fun cargarNota(){
        val notaID = this.intent.getLongExtra("id",-1L)
        val desc = "" + this.intent.getStringExtra("desc")
        val imgSrc = "" + this.intent.getStringExtra("srcImg")
        pathImagen = imgSrc
        imageView.loadUrl(imgSrc)
        nota = Nota(notaID, desc, imgSrc)
        textDescription.setText(desc)
    }

    private fun ImageView.loadUrl(url: String) {
        Picasso.get().load(url).into(imageView)
    }

    private fun buscarGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALERIA, Bundle())
    }

    private fun abrirCamara(){
        photo = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photo)
        startActivityForResult(intent, REQUEST_CAMERA, Bundle())
    }

    private fun guardar() {
        val descripcion = textDescription.text.toString()
        if(viewModelCrear.validarGuardar(descripcion, pathImagen, nota)){
            mostrarToast(getString(R.string.nota_guardada))
            onBackPressed()
        }else{
            mostrarToast(getString(R.string.debe_ingresar_una_descripcion_y_una_imagen))
        }
    }

    private fun tomarFotos() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val permisoCamara = checkSelfPermission(android.Manifest.permission.CAMERA)
            val permisoAlmacenamiento = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val denegado = PackageManager.PERMISSION_DENIED
            if(permisoCamara == denegado || permisoAlmacenamiento == denegado){
                val permisosCamara = arrayOf(android.Manifest.permission.CAMERA,
                                             android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permisosCamara, REQUEST_CAMERA)
            }else{
                abrirCamara()
            }
        }else{
            abrirCamara()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALERIA) {
                pathImagen = data!!.data.toString()
                imageView.setImageURI(data.data)
            }
            if (requestCode == REQUEST_CAMERA) {
                imageView.setImageURI(photo)
                pathImagen = photo.toString()
            }
        }
    }

    private fun mostrarToast(stringMostrar: String){
        Toast.makeText(
        applicationContext,
        stringMostrar,
        Toast.LENGTH_SHORT
        ).show()
    }
}