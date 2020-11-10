package com.example.notesapp.data

import com.example.notesapp.model.Nota

interface RoomNotasRepositoryInterface {
    suspend fun insertarNota(nota: Nota)
    suspend fun getAll(): List<Nota>
    suspend fun delete()
    suspend fun delete(nota: Nota)
}