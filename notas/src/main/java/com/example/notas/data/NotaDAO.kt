package com.example.notas.data

import androidx.room.*

@Dao
interface NotaDAO { // Acá también pueden ir insert, update, etc...
    @Query("SELECT * FROM notas")
    fun getAll():List<NotaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarNota(entity: NotaEntity)

    @Query("DELETE FROM notas")
    fun deleteAll()

    @Delete
    fun delete(entity: NotaEntity)
}