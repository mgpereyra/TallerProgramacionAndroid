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

    fun insertarEnDatabase(nota: NotaEntity) {
        viewModelScope.launch {
            notasRepository.insertarNota(nota)
        }
    }
}