package com.example.notesapp.data

import android.app.Application
import androidx.room.Room

class NotasApplication:Application(){
    val room = Room
        .databaseBuilder(this, NotaDatabase::class.java, "notas")
        .build()
}