package com.example.notesapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.NotaEntity
import com.example.notesapp.model.Nota
import com.example.notesapp.data.NotaRepository
import kotlinx.coroutines.launch

class CrearNotasViewModel(private val notasRepository: NotaRepository
    ) : ViewModel() {

    val status = MutableLiveData<Status>()

    fun validarGuardar(description: String, pathImagen: String, nota: Nota?): Boolean {
        try {
            if (description.trim() != "" && pathImagen != ""){
                if (nota != null) {
                    actualizarNota(
                        NotaEntity(
                            id = nota.id,
                            description = description,
                            srcImagen = pathImagen
                        )
                    )
                } else {
                    insertarEnDatabase(
                        NotaEntity(
                            description = description,
                            srcImagen = pathImagen
                        )
                    )
                }
                status.value = Status.SUCCESS
                return true
            } else {
                status.value = Status.ERROR
                return false
            }
        } catch (ignored: Exception){
            status.value = Status.ERROR
            return false
        }
    }

    fun actualizarNota(notaEntity: NotaEntity) {
        viewModelScope.launch {
            try {
                notasRepository.modificarNota(notaEntity)
                status.value = Status.SUCCESS
            }catch(e: Exception){
                status.value = Status.ERROR
            }
        }
    }

    fun insertarEnDatabase(nota: NotaEntity) {
        viewModelScope.launch {
            try {
                notasRepository.insertarNota(nota)
                status.value = Status.SUCCESS
            }catch(e: Exception){
                status.value = Status.ERROR
            }
        }
    }

    enum class Status{
        SUCCESS,
        ERROR
    }
}