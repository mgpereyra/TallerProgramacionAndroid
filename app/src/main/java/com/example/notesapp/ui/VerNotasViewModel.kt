package com.example.notesapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.model.Nota
import com.example.notesapp.data.NotaRepository
import kotlinx.coroutines.*

class VerNotasViewModel(private val notasRepository: NotaRepository
    ) : ViewModel() {

    val notasLiveData = MutableLiveData<List<Nota>>()

    init {
        viewModelScope.launch { getNotasList() }
    }

    fun actualizarLista(){
        viewModelScope.launch { getNotasList() }
    }

    suspend fun getNotasList() {
        notasLiveData.value = notasRepository.getAll()
    }

    fun borrarNota(nota: Nota){
        viewModelScope.launch { delete(nota) }
    }

    suspend fun delete(nota: Nota){
        notasRepository.delete(nota)
        notasLiveData.value = notasRepository.getAll()
    }
}