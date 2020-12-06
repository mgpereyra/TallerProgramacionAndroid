package com.example.notesapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.notesapp.CoroutineTestRule
import com.example.notesapp.data.NotaEntity
import com.example.notesapp.data.NotaRepository
import com.example.notesapp.model.Nota
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.RuntimeException

class CrearNotasViewModelTest{
    @MockK
    lateinit var instance: CrearNotasViewModel

    @MockK
    lateinit var notasRepository: NotaRepository

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @ExperimentalCoroutinesApi
    @Test
    fun `que una nota se cree correctamente`(){
        coroutineTestRule.testDispatcher.runBlockingTest {
            instance = CrearNotasViewModel(notasRepository)
            coJustRun { notasRepository.insertarNota(any()) }
            instance.status.observeForever{
                assertThat(it).isEqualTo(CrearNotasViewModel.Status.SUCCESS)
            }
            val nota = NotaEntity(1, "test","./test")
            instance.insertarEnDatabase(nota)
            coVerify { notasRepository.insertarNota(nota) }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `que insertar una nota se lance exception`(){
        coroutineTestRule.testDispatcher.runBlockingTest {
            instance = CrearNotasViewModel(notasRepository)
            coEvery { notasRepository.insertarNota(any()) } throws RuntimeException(
                "test_exception",
                RuntimeException("test")
            )
            instance.status.observeForever{
                assertThat(it).isEqualTo(CrearNotasViewModel.Status.ERROR)
            }
            val nota = NotaEntity(1, "test","./test")
            instance.insertarEnDatabase(nota)
            coVerify { notasRepository.insertarNota(nota) }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `que validar guardar nota sin description falle`(){
        coroutineTestRule.testDispatcher.runBlockingTest {
            instance = CrearNotasViewModel(notasRepository)
            coJustRun { notasRepository.insertarNota(any()) }
            instance.status.observeForever{
                assertThat(it).isEqualTo(CrearNotasViewModel.Status.ERROR)
            }
            instance.validarGuardar("","./test", null)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `que validar guardar nota sin foto falle`(){
        coroutineTestRule.testDispatcher.runBlockingTest {
            instance = CrearNotasViewModel(notasRepository)
            coJustRun { notasRepository.insertarNota(any()) }
            instance.status.observeForever{
                assertThat(it).isEqualTo(CrearNotasViewModel.Status.ERROR)
            }
            instance.validarGuardar("test","", null)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `que validar guardar nota sin foto ni description falle`(){
        coroutineTestRule.testDispatcher.runBlockingTest {
            instance = CrearNotasViewModel(notasRepository)
            coJustRun { notasRepository.insertarNota(any()) }
            instance.status.observeForever{
                assertThat(it).isEqualTo(CrearNotasViewModel.Status.ERROR)
            }
            instance.validarGuardar("","", null)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `que validar guardar nota sin foto ni description falle al pasarle una nota para actualizar`(){
        coroutineTestRule.testDispatcher.runBlockingTest {
            instance = CrearNotasViewModel(notasRepository)
            coJustRun { notasRepository.insertarNota(any()) }
            instance.status.observeForever {
                assertThat(it).isEqualTo(CrearNotasViewModel.Status.ERROR)
            }
            instance.validarGuardar("","", Nota(1, "test", "./test"))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `que insertar una nota arroje exception`() {
        coroutineTestRule.testDispatcher.runBlockingTest {
            instance = CrearNotasViewModel(notasRepository)
            coEvery { notasRepository.insertarNota(any()) } throws RuntimeException(
                "test_exception",
                RuntimeException("test")
            )
            instance.status.observeForever {
                assertThat(it).isEqualTo(CrearNotasViewModel.Status.ERROR)
            }
            instance.insertarEnDatabase(NotaEntity(1, "test", "./test"))
        }
    }
}