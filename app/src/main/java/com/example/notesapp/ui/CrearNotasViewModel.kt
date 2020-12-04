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
            if (nota == null) {
                if (description.trim() == "" || pathImagen == "") {
                    status.value = Status.ERROR
                    return false
                } else {
                    insertarEnDatabase(
                        NotaEntity(
                            description = description,
                            srcImagen = pathImagen
                        )
                    )
                    status.value = Status.SUCCESS
                    return true
                }
            } else {
                actualizarNota(
                    NotaEntity(
                        id = nota.id,
                        description = description,
                        srcImagen = pathImagen
                    )
                )
                status.value = Status.SUCCESS
                return true
            }
        } catch (ignored: Exception){
            status.value = Status.ERROR
            return false
        }
    }

    fun actualizarNota(notaEntity: NotaEntity) {
        viewModelScope.launch {
            notasRepository.modificarNota(notaEntity)
        }
    }

    fun insertarEnDatabase(nota: NotaEntity) {
        viewModelScope.launch {
            notasRepository.insertarNota(nota)
        }
    }

    enum class Status{
        SUCCESS,
        ERROR,
        NOT_VALID
    }
}