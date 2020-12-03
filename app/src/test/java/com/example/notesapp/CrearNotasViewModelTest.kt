package com.example.notesapp

import com.example.notesapp.data.NotaDAO
import com.example.notesapp.data.NotaEntity
import com.example.notesapp.data.RoomNotasRepository
import com.example.notesapp.ui.CrearNotasViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CrearNotasViewModelTest{

    @MockK
    lateinit var repository: RoomNotasRepository

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    public fun testing(){
        coEvery { repository.getAll()  }
        val vm = CrearNotasViewModel(repository)
        // vm.validarGuardar() ???
        // SE PRUEBAN LOS REPOSITORIOS (DE ROOM) y LOS VIEWMODELs

    }
}