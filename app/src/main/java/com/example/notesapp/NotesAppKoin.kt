package com.example.notesapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.notesapp.data.NotaDAO
import com.example.notesapp.data.NotaDatabase
import com.example.notesapp.data.RoomNotasRepository
import com.example.notesapp.data.NotaRepository
import com.example.notesapp.ui.CrearNotasViewModel
import com.example.notesapp.ui.VerNotasViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class NotesAppKoin : Application() {
    val appModule = module {
        single<NotaDAO>{ provideDatabase(get()).notaDAO() }
        single<NotaRepository> { RoomNotasRepository(get()) }
        viewModel { VerNotasViewModel(get()) }
        viewModel { CrearNotasViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@NotesAppKoin)
            modules(appModule)
        }
    }

    private fun provideDatabase(context: Context):NotaDatabase{
        return Room.databaseBuilder(context,NotaDatabase::class.java,"database-notas").build()
    }
}