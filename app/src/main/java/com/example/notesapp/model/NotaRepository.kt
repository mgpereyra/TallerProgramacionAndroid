package com.example.notesapp.model

interface NotaRepository {
    suspend fun insertarNota(nota: Nota)
    suspend fun getAll(): List<Nota>
    suspend fun delete(nota: Nota)
    suspend fun delete()
}