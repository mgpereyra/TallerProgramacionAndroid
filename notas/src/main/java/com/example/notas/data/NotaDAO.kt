package com.example.notas.data

import androidx.room.*

@Dao
interface NotaDAO { // Acá también pueden ir insert, update, etc...
    @Query("SELECT * FROM notas")
    suspend fun getAll():List<NotaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(entity: NotaEntity)

    @Delete
    suspend fun delete(entity: NotaEntity)
}