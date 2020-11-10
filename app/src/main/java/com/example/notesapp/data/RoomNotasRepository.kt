package com.example.notesapp.data

import com.example.notesapp.model.Nota
import com.example.notesapp.model.NotaRepository

class RoomNotasRepository (private val notaDAO: NotaDAO): NotaRepository, RoomNotasRepositoryInterface {

    override suspend fun insertarNota(nota: Nota) {
        val entity = NotaEntity(
            description = nota.description,
            srcImagen = nota.srcImagen
        )
        notaDAO.insertarNota(entity)
    }

    override suspend fun getAll(): List<Nota> {
        return notaDAO.getAll().map{Nota(id = it.id, description = it.description,srcImagen = it.srcImagen)}
    }

    override suspend fun delete(nota: Nota) {

    }

    override suspend fun delete() {

    }
}