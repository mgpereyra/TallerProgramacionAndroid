package com.example.notesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version=1,
    entities=[NotaEntity::class],
    exportSchema = false
)

abstract class NotaDatabase:RoomDatabase() {
    abstract fun notaDAO():NotaDAO
}