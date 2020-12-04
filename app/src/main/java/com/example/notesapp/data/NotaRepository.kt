package com.example.notesapp.data

import com.example.notesapp.model.Nota

interface NotaRepository {
    suspend fun insertarNota(nota: NotaEntity)
    suspend fun getAll(): List<Nota>
    suspend fun delete(nota: Nota)
    suspend fun delete()
    suspend fun modificarNota(nota: NotaEntity)
}