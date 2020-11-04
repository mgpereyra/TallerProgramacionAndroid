package com.example.crearnotas.model

import com.example.crearnotas.data.NotaEntity

interface NotaRepository {
    fun getAll():List<Nota>
    fun guardarNota(nota: Nota)
    fun delete(nota: Nota)
}