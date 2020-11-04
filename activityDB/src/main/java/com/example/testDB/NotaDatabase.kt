package com.example.testDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version=1,
    entities=[NotaEntity::class]
)

abstract class NotaDatabase:RoomDatabase() {
    abstract fun notaDAO():NotaDAO
}