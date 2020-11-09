package com.example.notas.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.notas.data.NotaDatabase
import com.example.notas.data.RoomNotasRepository

class NotasViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory{
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val database = Room.databaseBuilder(
            applicationContext,
            NotaDatabase::class.java,
            "database-notas"
        )
        .build()
        val dao = database.notaDAO()
        return NotasViewModel(RoomNotasRepository(dao)) as T
    }
}