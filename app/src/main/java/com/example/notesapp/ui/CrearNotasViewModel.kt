package com.example.notesapp.ui

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.R
import com.example.notesapp.data.NotaEntity
import com.example.notesapp.model.NotaRepository
import kotlinx.android.synthetic.main.activity_crear_notas.*
import kotlinx.coroutines.launch

class CrearNotasViewModel(private val notasRepository: NotaRepository
    ) : ViewModel() {

    lateinit var activityCrearNotas: CrearNotasActivity
    private val REQUEST_CAMERA = 1002

    fun buscarGaleria(crearNotasActivity: CrearNotasActivity) {
        activityCrearNotas = crearNotasActivity
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(crearNotasActivity, intent,100, Bundle())
    }

    fun tomarFotos(crearNotasActivity: CrearNotasActivity) {
        activityCrearNotas = crearNotasActivity
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(crearNotasActivity.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                ||crearNotasActivity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_DENIED){
                val permisosCamara = arrayOf(android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                crearNotasActivity.requestPermissions(permisosCamara,REQUEST_CAMERA)
            }else{
                val value = ContentValues()
                crearNotasActivity.photo = crearNotasActivity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,value)
                val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,crearNotasActivity.photo)
                startActivityForResult(crearNotasActivity,camaraIntent,REQUEST_CAMERA, Bundle())
            }
        }else{
            Toast.makeText(crearNotasActivity.applicationContext,
                crearNotasActivity.getString(R.string.funcion_no_disponible),
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun comprobarPermisos(crearNotasActivity: CrearNotasActivity) {
        crearNotasActivity.buttonSacarFotos.setOnClickListener {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if(crearNotasActivity.checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    ||crearNotasActivity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(crearNotasActivity.applicationContext,
                        crearNotasActivity.getString(R.string.camara_no_disponible),
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    fun guardar(crearNotasActivity: CrearNotasActivity) {
        val descripcion = crearNotasActivity.textDescription.text.toString()
        if(descripcion.trim()=="" || crearNotasActivity.pathImagen==""){
            Toast.makeText(
                crearNotasActivity.applicationContext,
                crearNotasActivity.getString(R.string.debe_ingresar_una_descripcion_y_una_imagen),
                Toast.LENGTH_SHORT
            ).show()
        }else{
            insertarEnDatabase(
                NotaEntity(
                    description = descripcion,
                    srcImagen = crearNotasActivity.pathImagen
                )
            )
            Toast.makeText(
                crearNotasActivity.applicationContext,
                crearNotasActivity.getString(R.string.nota_guardada),
                Toast.LENGTH_SHORT
            ).show()
            crearNotasActivity.onBackPressed()
        }
    }



    fun insertarEnDatabase(nota: NotaEntity) {
        viewModelScope.launch {
            notasRepository.insertarNota(nota)
        }
    }
}