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

    override suspend fun insertarNota(nota: NotaEntity) {
        notaDAO.insertarNota(nota)
    }

    override suspend fun getAll(): List<Nota> {
        return notaDAO.getAll().map{Nota(id = it.id, description = it.description,srcImagen = it.srcImagen)}
    }

    override suspend fun delete(nota: Nota) {
        notaDAO.delete(NotaEntity(nota.id,nota.description,nota.srcImagen))
    }

    override suspend fun delete() {

    }

    override suspend fun modificarNota(nota: NotaEntity) {
        notaDAO.modificarNota(nota)
    }
}