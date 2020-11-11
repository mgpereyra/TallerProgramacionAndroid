package com.example.notesapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.NotaEntity
import com.example.notesapp.model.Nota
import com.example.notesapp.model.NotaRepository
import kotlinx.coroutines.*

class NotasViewModel( private val notasRepository: NotaRepository
    ) : ViewModel() {

    val notasLiveData = MutableLiveData<List<Nota>>()

    init {
        viewModelScope.launch { getNotasList() }
    }

    suspend fun getNotasList() {
        notasLiveData.value = notasRepository.getAll()
    }

    suspend fun insertarEnDatabase(nota: NotaEntity){
        notasRepository.insertarNota(nota)
    }
}