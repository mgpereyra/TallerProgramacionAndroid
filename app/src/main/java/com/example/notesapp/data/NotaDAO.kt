package com.example.notesapp.data

import androidx.room.*

@Dao
interface NotaDAO {
    @Query("SELECT * FROM notas")
    suspend fun getAll():List<NotaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNota(entity: NotaEntity)

    @Query("DELETE FROM notas")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(entity: NotaEntity)

    @Update()
    suspend fun modificarNota(entity: NotaEntity)

}