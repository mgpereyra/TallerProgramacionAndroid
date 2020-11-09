package com.example.notas.model

import com.example.notas.model.Nota

interface NotasRepository {
    fun insertarNota(nota: Nota)
    fun getAll(): List<Nota>
}