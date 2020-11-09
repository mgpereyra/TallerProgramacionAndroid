package com.example.notas.data

import com.example.notas.model.Nota
import com.example.notas.model.NotasRepository

class RoomNotasRepository (private val notaDAO: NotaDAO): NotasRepository {

    // Acá me usa el suspend...
    override fun insertarNota(nota: Nota) {
        val entity = NotaEntity(
            description = nota.description,
            srcImagen = nota.image
        )
        notaDAO.insertarNota(entity)
    }

    override fun getAll(): List<Nota> {
        return notaDAO.getAll().map{Nota(it.description,it.srcImagen)}
    }
}