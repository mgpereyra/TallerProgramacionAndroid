package com.example.notesapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.notesapp.data.NotaDatabase
import com.example.notesapp.data.RoomNotasRepository

@Suppress("UNCHECKED_CAST")
class NotasViewModelFactory(private val applicationContext: Context) : ViewModelProvider.Factory{
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        val database = Room.databaseBuilder(
            applicationContext,
            NotaDatabase::class.java,
            "database-notas"
        )
        .build()
        val dao = database.notaDAO()
        return VerNotasViewModel(RoomNotasRepository(dao)) as T
    }
}