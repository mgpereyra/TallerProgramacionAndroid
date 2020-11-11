package com.example.notesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.NotaEntity
import com.example.notesapp.model.NotaRepository
import kotlinx.coroutines.launch

class CrearNotasViewModel(private val notasRepository: NotaRepository
    ) : ViewModel() {

    fun insertarEnDatabase(nota: NotaEntity) {
        viewModelScope.launch {
            notasRepository.insertarNota(nota)
        }
    }
}