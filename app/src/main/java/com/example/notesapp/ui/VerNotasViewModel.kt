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
    val status = MutableLiveData<Status>()

    init {
        viewModelScope.launch { getNotasList() }
    }

    fun actualizarLista(){
        getNotasList()
    }

    fun getNotasList() {
        viewModelScope.launch {
            try {
                notasLiveData.value = notasRepository.getAll()
                status.value = Status.SUCCESS
            } catch (e: Exception) {
                status.value = Status.ERROR
            }
        }
    }

    fun borrarNota(nota: Nota){
        viewModelScope.launch {
            try {
                delete(nota)
                status.value = Status.SUCCESS
            }catch(e: Exception){
                status.value = Status.ERROR
            }
        }
    }

    suspend fun delete(nota: Nota){
        try {
            notasRepository.delete(nota)
            notasLiveData.value = notasRepository.getAll()
            status.value = Status.SUCCESS
        }catch(e: Exception){
            status.value = Status.ERROR
        }
    }

    enum class Status{
        SUCCESS,
        ERROR
    }
}