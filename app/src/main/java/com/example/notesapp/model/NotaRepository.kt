package com.example.notesapp.model

import com.example.notesapp.data.NotaEntity

interface NotaRepository {
    suspend fun insertarNota(nota: NotaEntity)
    suspend fun getAll(): List<Nota>
    suspend fun delete(nota: Nota)
    suspend fun delete()
    suspend fun modificarNota(nota: NotaEntity)
}