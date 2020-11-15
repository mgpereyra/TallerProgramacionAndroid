package com.example.notesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.NotaEntity
import com.example.notesapp.model.Nota
import com.example.notesapp.model.NotaRepository
import kotlinx.coroutines.launch

class CrearNotasViewModel(private val notasRepository: NotaRepository
    ) : ViewModel() {

    fun validarGuardar(description: String, pathImagen: String, nota: Nota?): Boolean {
        if(nota==null) {
            if (description.trim() == "" || pathImagen == "") {
                return false
            } else {
                insertarEnDatabase(
                    NotaEntity(
                        description = description,
                        srcImagen = pathImagen
                    )
                )
                return true
            }
        }else{
            actualizarNota(
                NotaEntity(
                    id = nota.id,
                    description = description,
                    srcImagen = pathImagen
                )
            )
            return true
        }
    }

    private fun actualizarNota(notaEntity: NotaEntity) {
        viewModelScope.launch {
            notasRepository.modificarNota(notaEntity)
        }
    }

    private fun insertarEnDatabase(nota: NotaEntity) {
        viewModelScope.launch {
            notasRepository.insertarNota(nota)
        }
    }
}